package ru.customer.petproject.customerservice.unittests.util;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.util.CustomerMapper;
import ru.zinovievbank.customerservice.util.CustomerMapperImpl;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    private final String FIRST_NAME = "Alex";
    private final String LAST_NAME = "Grey";
    private final String PATRONYMIC = "Patronymic";

    private final String EMAIL = "test@test.te";
    private final String MOBILE_PHONE = "12345678901";
    private final UUID CUSTOMER_ID = UUID.randomUUID();
    private Customer customer;

    private final CustomerMapper mapper = new CustomerMapperImpl(new CustomerStatusConverter());

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setMobilePhone(MOBILE_PHONE);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPatronymic(PATRONYMIC);
        customer.setBirthDate(LocalDate.now());
        customer.setCustomerStatus(CustomerStatus.CUSTOMER_NOT_REGISTERED);
        customer.setEmail(EMAIL);
    }

    @Test
    @DisplayName("Should properly map Customer to CustomerInfoDto")
    void shouldProperlyMapClientToClientInfoDto() {
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        customerInfoDto.setFirstName(FIRST_NAME);
        customerInfoDto.setLastName(LAST_NAME);
        customerInfoDto.setPatronymic(PATRONYMIC);
        customerInfoDto.setBirthDate(LocalDate.now());
        customerInfoDto.setMobilePhone(MOBILE_PHONE);
        customerInfoDto.setEmail(EMAIL);
        customerInfoDto.setCustomerStatus(CustomerStatus.CUSTOMER_NOT_REGISTERED.getCode());

        CustomerInfoDto res = mapper.toCustomerInfoDto(customer);

        Assertions.assertEquals(customerInfoDto, res);
    }

    @Test
    @DisplayName("Should properly map Customer to CustomerInfoDto")
    void shouldProperlyMapClientToClientNotificationsDto() {
        CustomerNotificationsDto customerNotificationsDto = new CustomerNotificationsDto();
        customerNotificationsDto.setEmail(EMAIL);

        CustomerNotificationsDto res = mapper.toCustomerNotificationsDto(customer);

        Assertions.assertEquals(customerNotificationsDto, res);
    }

    @Test
    @DisplayName("Should update Customer for email")
    void shouldUpdateClientForEmail() {
        String testEmail = "testEmail@test.ru";

        Customer customerAfterUpdate = new Customer();
        customerAfterUpdate.setEmail(testEmail);

        EmailDto emailDto = new EmailDto(testEmail);

        mapper.updateCustomerForEmail(emailDto, customer);

        Assertions.assertEquals(customerAfterUpdate.getEmail(), customer.getEmail());
    }

    @Test
    @DisplayName("Should update Customer for SecurityQuestionAndAnswerDto")
    void shouldUpdateCustomerForSecurityQuestionAndAnswer() {
        String testSecurityQuestion = "test";
        String testSecurityAnswer = "test";

        Customer customerAfterUpdate = new Customer();
        customerAfterUpdate.setSecurityQuestion(testSecurityQuestion);
        customerAfterUpdate.setSecurityAnswer(testSecurityAnswer);

        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto = new SecurityQuestionAndAnswerDto(
            testSecurityQuestion, testSecurityAnswer);

        mapper.updateCustomerForSecurityQuestionAndAnswer(securityQuestionAndAnswerDto, customer);

        Assertions.assertEquals(customerAfterUpdate.getSecurityQuestion(),
            customer.getSecurityQuestion());
        Assertions.assertEquals(customerAfterUpdate.getSecurityAnswer(),
            customer.getSecurityAnswer());
    }

    @Test
    @DisplayName("Should update notification sms")
    void shouldUpdateNotificationSms() {
        boolean notificationStatus = true;

        Customer customerAfterUpdate = new Customer();
        customerAfterUpdate.setSmsNotification(notificationStatus);

        NotificationStatusDto notificationStatusDto = new NotificationStatusDto();
        notificationStatusDto.setNotificationStatus(notificationStatus);

        mapper.updateNotificationSms(notificationStatusDto, customer);

        Assertions.assertEquals(customerAfterUpdate.getSmsNotification(),
            customer.getSmsNotification());
    }

    @Test
    @DisplayName("Should update notification email")
    void shouldUpdateNotificationEmail() {
        boolean notificationStatus = true;

        Customer customerAfterUpdate = new Customer();
        customerAfterUpdate.setEmailSubscription(notificationStatus);

        NotificationStatusDto notificationStatusDto = new NotificationStatusDto();
        notificationStatusDto.setNotificationStatus(notificationStatus);

        mapper.updateNotificationEmail(notificationStatusDto, customer);

        Assertions.assertEquals(customerAfterUpdate.getEmailSubscription(),
            customer.getEmailSubscription());
    }

    @Test
    @DisplayName("Should update notification push")
    void shouldUpdateNotificationPush() {
        boolean notificationStatus = true;

        Customer customerAfterUpdate = new Customer();
        customerAfterUpdate.setPushNotification(notificationStatus);

        NotificationStatusDto notificationStatusDto = new NotificationStatusDto();
        notificationStatusDto.setNotificationStatus(notificationStatus);

        mapper.updateNotificationPush(notificationStatusDto, customer);

        Assertions.assertEquals(customerAfterUpdate.getPushNotification(),
            customer.getPushNotification());
    }
}
