package ru.customer.petproject.customerservice.unittests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;
import ru.zinovievbank.customerservice.kafka.KafkaProducer;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.service.NotificationService;
import ru.zinovievbank.customerservice.util.CustomerMapper;
import ru.zinovievbank.customerservice.util.CustomerMapperImpl;
import ru.zinovievbank.customerservice.auth.JwtProvider;
import ru.zinovievbank.customerservice.util.enums.NotificationType;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.customer.petproject.customerservice.unittests.util.TestConstants.REFRESH_TOKEN;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    private final String EMAIL = "test@test.te";
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private final boolean DEFAULT_SMS_NOTIFICATION_STATUS = false;
    private final boolean DEFAULT_PUSH_NOTIFICATION_STATUS = true;
    private final boolean DEFAULT_EMAIL_SUBSCRIPTION = false;

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private Map<NotificationType, Integer> partitionMap;
    @Mock
    private KafkaProducer kafkaProducer;

    @SuppressWarnings("unused")
    @Spy
    private final CustomerMapper mapper = new CustomerMapperImpl(new CustomerStatusConverter());

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        partitionMap = new HashMap<>();
        partitionMap.put(NotificationType.SMS, 0);
        partitionMap.put(NotificationType.EMAIL, 1);
        partitionMap.put(NotificationType.PUSH, 2);
    }

    private Customer getDefaultCustomer() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setEmail(EMAIL);
        customer.setPushNotification(DEFAULT_PUSH_NOTIFICATION_STATUS);
        customer.setEmailSubscription(DEFAULT_EMAIL_SUBSCRIPTION);
        customer.setSmsNotification(DEFAULT_SMS_NOTIFICATION_STATUS);
        return customer;
    }

    @Test
    @DisplayName("CRS-12: should set notification sms if id is valid")
    void setNotificationSms_ShouldSetNotificationSms_IfIdIsValid() {
        Customer customer = getDefaultCustomer();
        boolean newNotificationStatus = !DEFAULT_SMS_NOTIFICATION_STATUS;
        NotificationStatusDto notificationStatusDto =
            new NotificationStatusDto(newNotificationStatus);
        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        notificationService.setNotificationSms(REFRESH_TOKEN, notificationStatusDto);
        assertEquals(newNotificationStatus, customer.getSmsNotification());
    }

    @Test
    @DisplayName("CRS-12: should throw an AppException if id does not exist")
    void setNotificationSms_ShouldThrowException_IfIdDoesNotExist() {
        boolean newNotificationStatus = !DEFAULT_SMS_NOTIFICATION_STATUS;
        NotificationStatusDto notificationStatusDto = new NotificationStatusDto(
            newNotificationStatus);

        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID))
            .thenReturn(Optional.empty());

        AppException appException = assertThrows(AppException.class,
            () -> notificationService.setNotificationSms(REFRESH_TOKEN, notificationStatusDto));
        assertEquals(EnumException.BAD_REQUEST, appException.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-12: should throw an AppException if token is invalid")
    void setNotificationSms_ShouldThrowException_IfIdIsInvalid() {
        String INVALID_REFRESH_TOKEN = REFRESH_TOKEN + "broken";
        boolean newNotificationStatus = !DEFAULT_SMS_NOTIFICATION_STATUS;
        NotificationStatusDto notificationStatusDto = new NotificationStatusDto(
            newNotificationStatus);

        AppException appException = assertThrows(
            AppException.class,
            () -> notificationService.setNotificationSms(INVALID_REFRESH_TOKEN,
                notificationStatusDto));
        assertEquals(EnumException.BAD_REQUEST, appException.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-13: should set email notification if id is valid")
    void setNotificationEmail_ShouldSetEmailNotification_IfIdIsValid() {
        Customer customer = getDefaultCustomer();
        boolean newNotificationStatus = !DEFAULT_EMAIL_SUBSCRIPTION;
        NotificationStatusDto notificationStatusDto =
            new NotificationStatusDto(newNotificationStatus);

        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        notificationService.setNotificationEmail(REFRESH_TOKEN, notificationStatusDto);
        assertEquals(!DEFAULT_EMAIL_SUBSCRIPTION, customer.getEmailSubscription());
    }

    @Test
    @DisplayName("CRS-13: should throw an AppException if id does not exist")
    void setNotificationEmail_ShouldThrowException_IfIdDoesNotExist() {
        boolean newNotificationStatus = !DEFAULT_EMAIL_SUBSCRIPTION;
        NotificationStatusDto notificationStatusDto =
            new NotificationStatusDto(newNotificationStatus);

        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        AppException appException = assertThrows(
            AppException.class,
            () -> notificationService.setNotificationEmail(REFRESH_TOKEN,
                notificationStatusDto));
        assertEquals(EnumException.BAD_REQUEST, appException.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-13: should throw an AppException if token is invalid")
    void setNotificationEmail_ShouldThrowException_IfIdIsInvalid() {
        boolean newNotificationStatus = !DEFAULT_EMAIL_SUBSCRIPTION;
        NotificationStatusDto notificationStatusDto = new NotificationStatusDto(
            newNotificationStatus);

        AppException appException = assertThrows(
            AppException.class,
            () -> notificationService.setNotificationEmail(REFRESH_TOKEN, notificationStatusDto));
        assertEquals(EnumException.BAD_REQUEST, appException.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-13: should throw an AppException if email is null")
    void setNotificationEmail_ShouldThrowException_IfEmailIsNull() {
        Customer customer = getDefaultCustomer();
        customer.setEmail(null);
        boolean newNotificationStatus = !DEFAULT_EMAIL_SUBSCRIPTION;
        NotificationStatusDto notificationStatusDto = new NotificationStatusDto(
            newNotificationStatus);
        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        AppException appException = assertThrows(
            AppException.class,
            () -> notificationService.setNotificationEmail(REFRESH_TOKEN,
                notificationStatusDto));
        assertEquals(EnumException.BAD_REQUEST, appException.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-10: should return CustomerNotificationDto if customer exist")
    void getCustomerNotificationsSettings_ShouldReturnCustomerNotificationDto_IfCustomerExist() {
        Customer customer = getDefaultCustomer();

        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerNotificationsDto customerNotificationsDto =
            notificationService.getCustomerNotificationsSettings(REFRESH_TOKEN);

        assertEquals(EMAIL, customerNotificationsDto.getEmail());
        assertEquals(DEFAULT_EMAIL_SUBSCRIPTION, customerNotificationsDto.getEmailSubscription());
        assertEquals(DEFAULT_SMS_NOTIFICATION_STATUS,
            customerNotificationsDto.getSmsNotification());
        assertEquals(
            DEFAULT_PUSH_NOTIFICATION_STATUS, customerNotificationsDto.getPushNotification());
    }

    @Test
    @DisplayName("CRS-10: should throw an AppException if id does not exist")
    void getCustomerNotificationsSettings_ShouldThrowException_IfIdDoesNotExist() {
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        when(jwtProvider.extractCustomerId(anyString()))
            .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        var customerIdString = CUSTOMER_ID.toString();
        AppException exception = assertThrows(
            AppException.class,
            () -> notificationService.getCustomerNotificationsSettings(REFRESH_TOKEN));

        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-10: should throw an AppException if token is invalid")
    void getCustomerNotificationsSettings_ShouldThrowException_IfIdIsInvalid() {
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        AppException exception = assertThrows(
            AppException.class,
            () -> notificationService.getCustomerNotificationsSettings(REFRESH_TOKEN));

        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }
}
