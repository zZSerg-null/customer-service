package ru.zinovievbank.customerservice.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.grpc.server.CustomerResponse;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.customer.petproject.customerservice.unittests.util.MockClassCustomResponse;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerMapperGrpcTest {
    private MockClassCustomResponse mockClassCustomResponse;
    private CustomerResponse customerResponse;
    private CustomerMapperGrpc customerMapperGrpc;

    @BeforeEach
    void setUp() {
        mockClassCustomResponse = new MockClassCustomResponse();
        customerResponse = mockClassCustomResponse.getCustomerResponseWithAllFields();
        customerMapperGrpc = new CustomerMapperGrpc();
    }

    @Test
    @DisplayName("Map CustomerResponse to Customer with all fields")
    void toCustomerWith_AllFields() {
        //todo: как сделать в assertEquals timestamp and LocalDate? убрать hardcode
        Customer customer = customerMapperGrpc.mapFromCustomerResponseToCustomer(customerResponse);

        assertEquals(customerResponse.getCustomerUuid(), customer.getId().toString());
        assertEquals(customerResponse.getFirstName(), customer.getFirstName());
        assertEquals(customerResponse.getLastName(), customer.getLastName());
        assertEquals(customerResponse.getPatronymic(), customer.getPatronymic());
        assertEquals("2024-04-05", customer.getBirthDate().toString());
        assertEquals(customerResponse.getMobilePhone(), customer.getMobilePhone());
        assertEquals(customerResponse.getEmail(), customer.getEmail());
        assertEquals(customerResponse.getInn(), customer.getInn());
        assertEquals("2024-04-05", customer.getAccessionDate().toString());
        assertEquals(CustomerStatus.CUSTOMER_REGISTERED, customer.getCustomerStatus());
        assertEquals(customerResponse.getSecurityQuestion(), customer.getSecurityQuestion());
        assertEquals(customerResponse.getSecurityAnswer(), customer.getSecurityAnswer());
        assertEquals(customerResponse.getSmsNotification(), customer.getSmsNotification());
        assertEquals(customerResponse.getPushNotification(), customer.getPushNotification());
        assertEquals(customerResponse.getEmailSubscription(), customer.getEmailSubscription());

        Passport passport = customer.getPassport();
        assertEquals(customerResponse.getCitizenship(), passport.getCitizenship());
        assertEquals(customerResponse.getSeries(), passport.getSeries());
        assertEquals(customerResponse.getNumber(), passport.getNumber());
        assertEquals("2024-04-05", passport.getIssuanceDate().toString());
        assertEquals(customerResponse.getDepartamentCode(), passport.getDepartmentCode());
        assertEquals(customerResponse.getIssuedBy(), passport.getIssuedBy());

        Address address = customer.getAddress();
        assertEquals(customerResponse.getCountry(), address.getCountry());
        assertEquals(customerResponse.getRegion(), address.getRegion());
        assertEquals(customerResponse.getCity(), address.getCity());
        assertEquals(customerResponse.getStreet(), address.getStreet());
        assertEquals(customerResponse.getHouseNumber(), address.getHouseNumber());
        assertEquals(String.valueOf(customerResponse.getEntranceNumber()), address.getEntranceNumber());
        assertEquals(customerResponse.getApartmentNumber(), address.getApartmentNumber());
        assertEquals(customerResponse.getPostcode(), address.getPostcode());
        assertEquals(customerResponse.getOktmo(), address.getOktmo());

        assertEquals(customer, address.getCustomer());
        assertEquals(customer, passport.getCustomer());
    }


    @Test
    @DisplayName("Customer with not registered status")
    void mapClientToCustomer_NotRegisteredStatus() {

        Customer customer = customerMapperGrpc
                .mapFromCustomerResponseToCustomer(mockClassCustomResponse.getCustomerResponseWithNotRegisteredStatus());

        assertEquals(CustomerStatus.CUSTOMER_NOT_REGISTERED, customer.getCustomerStatus());
    }

    @Test
    @DisplayName("Customer with blocked status")
    void mapClientToCustomer_BlockedStatus() {

        Customer customer = customerMapperGrpc
                .mapFromCustomerResponseToCustomer(mockClassCustomResponse.getCustomerResponseWithBlockedStatus());

        assertEquals(CustomerStatus.CUSTOMER_BLOCKED, customer.getCustomerStatus());
    }


    @Test
    @DisplayName("Customer with unknown status")
    void mapClientToCustomer_UnknownStatus() {

        AppException thrown = assertThrows(AppException.class, () -> {
            customerMapperGrpc.mapFromCustomerResponseToCustomer(mockClassCustomResponse.getCustomerResponseWithIncorrectStatus());
        });

        assertEquals("Unknown customer status", thrown.getMessage());
    }

}