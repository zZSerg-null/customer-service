package ru.customer.petproject.customerservice.unittests.util;

import java.time.LocalDate;
import java.util.UUID;
import ru.zinovievbank.customerservice.model.Customer;

public class MockClassCustomerForCustomerGrpcService {

    public static final String CUSTOMER_ID = "d54eb158-7499-4bda-bafb-d4bd965a1985";
    private static final String FIRST_NAME = "Alex";
    private static final String LAST_NAME = "Grey";
    private static final String PATRONYMIC = "Smithson";
    private static final String INN = "123456789101";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1985, 2, 12);
    private static final String MOBILE_PHONE = "79123456789";
    private static final String EMAIL = "example@gmail.com";
    private static final boolean SMS_NOTIFICATION = true;
    private static final boolean PUSH_NOTIFICATION = true;
    private static final boolean EMAIL_NOTIFICATION = true;

    public Customer getCustomerForCustomerGrpcService() {
        Customer customer = new Customer();
        MockClassAddressForCustomerGrpcService mockClassAddressForCustomerGrpcService
            = new MockClassAddressForCustomerGrpcService();
        MockClassPassportForCustomerGrpcService mockClassPassportForCustomerGrpcService
            = new MockClassPassportForCustomerGrpcService();
        customer.setId(UUID.fromString(CUSTOMER_ID));
        customer.setAddress(mockClassAddressForCustomerGrpcService.getAddress());
        customer.setPassport(mockClassPassportForCustomerGrpcService.getPassport());
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPatronymic(PATRONYMIC);
        customer.setBirthDate(BIRTH_DATE);
        customer.setInn(INN);
        customer.setMobilePhone(MOBILE_PHONE);
        customer.setEmail(EMAIL);
        customer.setSmsNotification(SMS_NOTIFICATION);
        customer.setPushNotification(PUSH_NOTIFICATION);
        customer.setEmailSubscription(PUSH_NOTIFICATION);
        return customer;
    }
}
