package ru.zinovievbank.customerservice.service;

import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.mapper.KafkaCustomerMapper;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.zinovievbank.customerservice.repository.AddressRepository;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.repository.PassportRepository;
import ru.zinovievbank.customerservice.util.CustomerMapper;
import ru.zinovievbank.customerservice.auth.JwtProvider;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;
    private final KafkaCustomerMapper kafkaCustomerMapper;
    private final AddressRepository addressRepository;
    private final PassportRepository passportRepository;
    private final JwtProvider jwtProvider;
    private final CustomerGrpcService customerGrpcService;


    public CustomerService(CustomerRepository customerRepository, CustomerMapper mapper,
                           KafkaCustomerMapper kafkaCustomerMapper,
                           AddressRepository addressRepository,
                           PassportRepository passportRepository, JwtProvider jwtProvider, CustomerGrpcService customerGrpcService) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
        this.kafkaCustomerMapper = kafkaCustomerMapper;
        this.addressRepository = addressRepository;
        this.passportRepository = passportRepository;
        this.jwtProvider = jwtProvider;
        this.customerGrpcService = customerGrpcService;
    }

    public boolean isCustomerPresent(String customerUUID) {
        return customerRepository.findCustomerById(UUID.fromString(customerUUID)).isPresent();
    }

    public UUID getCustomerIdByMobilePhone(String mobilePhone) {
        return customerRepository.findCustomerIdByMobilePhone(mobilePhone)
               .orElseThrow(() -> new AppException(EnumException.BAD_REQUEST));
    }

    /**
     * A method that sends information with the customer's personal data for later display. in the
     * main menu and personal account when viewing general information about the customer.
     *
     * @param token
     * @return {@link CustomerInfoDto}
     */
    @Cacheable(value = "customerInfo", key = "#token")
    public String getCustomerInfoById(String token) {
        UUID customerId = getUuid(token);
        CustomerInfoDto customerInfoDto = customerRepository.findById(customerId)
                .map(mapper::toCustomerInfoDto)
                .orElseThrow(() -> new AppException(EnumException.BAD_REQUEST));

        try {
            return Jackson2ObjectMapperBuilder.json()
                    .modules(new JavaTimeModule())
                    .build()
                    .writeValueAsString(customerInfoDto);
        } catch (JsonProcessingException e) {
            throw new AppException(EnumException.BAD_REQUEST);
        }
    }

    /**
     * The method allows you to update the email address associated with the Customer account in the
     * database.
     *
     * @param token
     * @param emailDto {@link EmailDto}
     */
    @Transactional
    @CacheEvict(value = "customerInfo", key = "#token")
    public void updateEmail(String token, EmailDto emailDto) {
        UUID customerId = getUuid(token);
        customerRepository.findById(customerId)
                .ifPresentOrElse(
                        customer -> mapper.updateCustomerForEmail(emailDto, customer),
                        () -> {
                            throw new AppException(EnumException.BAD_REQUEST);
                        });
    }

    /**
     * Method for changing the security question and answering it in the Customer’s personal
     * account.
     *
     * @param token
     * @param securityQuestionAndAnswerDto {@link SecurityQuestionAndAnswerDto}
     */
    @Transactional
    @CacheEvict(value = "customerInfo", key = "#token")
    public void updateSecurityQuestionAndAnswer(String token,
                                                SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto) {
        UUID customerId = getUuid(token);
        customerRepository.findById(customerId)
                .ifPresentOrElse(
                        customer -> mapper.updateCustomerForSecurityQuestionAndAnswer(
                                securityQuestionAndAnswerDto, customer),
                        () -> {
                            throw new AppException(EnumException.BAD_REQUEST);
                        });
    }

    /**
     * Method for checking a customer in a database by phone number
     *
     * @param phoneNumber
     */
    @Transactional
    public void checkCustomerByPhoneNumber(String phoneNumber) {
        Optional<Customer> customer = customerRepository.findCustomerByMobilePhone(phoneNumber);
        log.debug("Response for front: {}", customer);

        if (customer.isEmpty()) {
            customer = customerGrpcService.getCustomerByMobilePhone(phoneNumber);
            if (customer.isEmpty()) {
                throw new AppException(EnumException.NOT_FOUND, "Пользователь не существует");
            }
            customerRepository.save(customer.get());
            throw new AppException(EnumException.BAD_REQUEST, "Пользователь не зарегистрирован");

        } else {
            switch (customer.get().getCustomerStatus()) {
                case CUSTOMER_BLOCKED:
                    throw new AppException(EnumException.FORBIDDEN, "Пользователь заблокирован");
                case CUSTOMER_NOT_REGISTERED:
                    throw new AppException(EnumException.BAD_REQUEST, "Пользователь не зарегистрирован");
            }
        }
    }


    /**
     * Adds new consumer to the database given by ABS service using Kafka.
     *
     * @param dto {@link KafkaCustomerDto}
     */
    @Transactional
    public void addNewCustomer(KafkaCustomerDto dto) {
        if (customerRepository.existsById(dto.customerUuid())) {
            log.error("CustomerUUID: {} presents in DataBase", dto.customerUuid());
            return;
        }
        Customer customer = kafkaCustomerMapper.toCustomer(dto);
        Address address = kafkaCustomerMapper.toAddress(dto);
        Passport passport = kafkaCustomerMapper.toPassport(dto);

        addressRepository.save(address);
        passportRepository.save(passport);

        customer.setAddress(address);
        customer.setPassport(passport);

        customerRepository.save(customer);
    }

    /**
     * Updates a consumer in the database given by ABS service using Kafka.
     *
     * @param dto {@link KafkaUpdateCustomerDto}
     */
    @Transactional
    public void updateCustomer(KafkaUpdateCustomerDto dto) {
        Optional<Customer> optionalCustomer = customerRepository.findFullCustomerInfoById(dto.customerUuid());
        if (optionalCustomer.isEmpty()) {
            log.error("CustomerUUID: {} not found in DataBase", dto.customerUuid());
            return;
        }
        Customer customer = optionalCustomer.get();

        kafkaCustomerMapper.updateCustomer(dto, customer);
        kafkaCustomerMapper.updateAddress(dto, customer.getAddress());
        kafkaCustomerMapper.updatePassport(dto, customer.getPassport());

        customerRepository.save(customer);
    }

    /**
     * The method converts the incoming string parameter to UUID
     *
     * @param token
     * @return token as UUID
     */
    private UUID getUuid(String token) {
        log.info("Checking refresh token for validity");
        String refreshToken = token.substring(7);
        return jwtProvider.extractCustomerId(refreshToken);
    }
}

