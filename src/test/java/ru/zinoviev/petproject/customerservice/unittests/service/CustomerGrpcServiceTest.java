package ru.customer.petproject.customerservice.unittests.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.grpc.server.CustomerAvailablePhoneRequest;
import ru.aston.grpc.server.CustomerResponse;
import ru.aston.grpc.server.ServerGrpc;
import ru.zinovievbank.customerservice.mapper.CustomerMapperGrpc;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.service.CustomerGrpcService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerGrpcServiceTest {

    @Mock
    ServerGrpc.ServerBlockingStub serverStub;
    @Mock
    CustomerMapperGrpc customerMapperGrpc;

    @InjectMocks
    CustomerGrpcService customerGrpcService;


    @Test
    @DisplayName("get Optional<Customer> by mobilePhone")
    void getCustomerByMobilePhone_shouldCallServerStubAndCustomerMapperGrpc() {

        customerGrpcService.setServerStub(serverStub);

        CustomerResponse response = mock(CustomerResponse.class);
        when(serverStub.getCustomerAvailablePhone(any(CustomerAvailablePhoneRequest.class))).thenReturn(response);

        Customer customer = mock(Customer.class);
        when(customerMapperGrpc.mapFromCustomerResponseToCustomer(response)).thenReturn(customer);

        Optional<Customer> customerOptional =
                customerGrpcService.getCustomerByMobilePhone("1234567890");

        verify(customerMapperGrpc).mapFromCustomerResponseToCustomer(response);

        Assertions.assertNotNull(customerOptional);


    }
}