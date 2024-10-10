package ru.zinovievbank.customerservice.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaNotificationDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.kafka.KafkaProducer;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.util.CustomerMapper;
import ru.zinovievbank.customerservice.auth.JwtProvider;
import ru.zinovievbank.customerservice.util.enums.NotificationType;

@Slf4j
@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final KafkaProducer kafkaProducer;
    private final JwtProvider jwtProvider;

    public NotificationService(CustomerRepository customerRepository, CustomerMapper mapper,
        KafkaProducer kafkaProducer, JwtProvider jwtProvider) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.jwtProvider = jwtProvider;
    }

    /**
     * This method calls the corresponding method from the service, which allows you to make changes
     * to the Customer account in terms of settings for receiving SMS notifications.
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     */
    @Transactional
    @CacheEvict(value = "customerNotificationsSettings", key = "#token")
    public void setNotificationSms(String token, NotificationStatusDto statusDto) {
        UUID customerId = getUuid(token);
        customerRepository.findById(customerId)
            .ifPresentOrElse(customer -> {
                    mapper.updateNotificationSms(statusDto, customer);
                    sendKafkaMessageDto(customerId.toString(), NotificationType.SMS,
                        statusDto.getNotificationStatus());
                },
                () -> {
                    throw new AppException(EnumException.BAD_REQUEST);
                });
    }

    /**
     * Method for updating the Customer account in terms of settings for receiving email
     * newsletters.
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     */
    @Transactional
    @CacheEvict(value = "customerNotificationsSettings", key = "#token")
    public void setNotificationEmail(String token, NotificationStatusDto statusDto) {
        UUID customerId = getUuid(token);
        customerRepository.findById(customerId)
            .ifPresentOrElse(customer -> {
                if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
                    throw new AppException(EnumException.BAD_REQUEST);
                }
                mapper.updateNotificationEmail(statusDto, customer);
                sendKafkaMessageDto(customerId.toString(), NotificationType.EMAIL,
                    statusDto.getNotificationStatus());
            },
            () -> {
                throw new AppException(EnumException.BAD_REQUEST);
            });
    }

    /**
     * Method that updates Customer accounts regarding notification settings
     *
     * @param token
     * @param statusDto  {@link NotificationStatusDto}
     */
    @Transactional
    @CacheEvict(value = "customerNotificationsSettings", key = "#token")
    public void setNotificationPush(String token, NotificationStatusDto statusDto) {
        UUID customerId = getUuid(token);
        customerRepository.findById(customerId)
            .ifPresentOrElse(customer -> {
                    mapper.updateNotificationPush(statusDto, customer);
                    sendKafkaMessageDto(customerId.toString(), NotificationType.PUSH,
                        statusDto.getNotificationStatus());
                },
                () -> {
                    throw new AppException(EnumException.BAD_REQUEST);
                });
    }

    @Cacheable(value = "customerNotificationsSettings", key = "#token")
    public CustomerNotificationsDto getCustomerNotificationsSettings(String token) {
        UUID customerId = getUuid(token);
        return customerRepository.findById(customerId)
            .map(mapper::toCustomerNotificationsDto)
            .orElseThrow(() -> new AppException(EnumException.BAD_REQUEST));
    }

    /**
     * The method converts the incoming string parameter to UUID
     *
     * @param token
     * @return id as UUID
     */
    private UUID getUuid(String token) {
        log.info("Checking refresh token for validity");
        String refreshToken = token.substring(7);
        return jwtProvider.extractCustomerId(refreshToken);
    }

    private void sendKafkaMessageDto(String customerId, NotificationType notificationType,
        Boolean notificationStatus) {
        KafkaNotificationDto messageDto = new KafkaNotificationDto();
        messageDto.setCustomerId(customerId);
        messageDto.setNotificationType(notificationType.toString());
        messageDto.setNotificationStatus(notificationStatus);

        kafkaProducer.sendMessage(notificationType, messageDto);
    }
}
