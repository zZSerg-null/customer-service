package ru.customer.petproject.customerservice.unittests.service;

import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.aston.globalexeptionsspringbootstarter.exception.ResourceNotFoundException;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.service.CustomerDataGrpcService;
import ru.customer.petproject.customerservice.unittests.util.MockClassCustomerForCustomerGrpcService;

import java.util.Optional;
import java.util.UUID;

import static customerservice.CustomerData.CustomerDataRequest;
import static customerservice.CustomerData.CustomerDataResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(classes = CustomerDataGrpcServiceTestConfig.class)
class CustomerDataGrpcServiceTest {

    public static final String CUSTOMER_ID = "d54eb158-7499-4bda-bafb-d4bd965a1985";

    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    CustomerDataGrpcService service;

    @Test
    @DisplayName("Positive get CustomerInfo by id test")
    void getCustomerInfoByIdTest_Positive() {
        MockClassCustomerForCustomerGrpcService mockClassCustomerForCustomerGrpcService
            = new MockClassCustomerForCustomerGrpcService();
        Customer customer = mockClassCustomerForCustomerGrpcService.getCustomerForCustomerGrpcService();

        given(customerRepository.findFullCustomerInfoById(UUID.fromString(CUSTOMER_ID)))
            .willReturn(Optional.of(customer));

        CustomerDataRequest request = CustomerDataRequest.newBuilder()
            .setCustomerId(CUSTOMER_ID)
            .build();

        StreamRecorder<CustomerDataResponse> responseObserver = StreamRecorder.create();

        service.getCustomerData(request, responseObserver);
        var values = responseObserver.getValues();
        assertEquals(1, values.size());
    }

    @Test
    @DisplayName("Negative get CustomerInfo by id test")
    void getCustomerInfoByIdTest_Negative() {
        given(customerRepository.findFullCustomerInfoById(UUID.fromString(CUSTOMER_ID)))
            .willReturn(Optional.empty());

        CustomerDataRequest request = CustomerDataRequest.newBuilder()
            .setCustomerId(CUSTOMER_ID)
            .build();

        StreamRecorder<CustomerDataResponse> responseObserver = StreamRecorder.create();

        assertThrows(ResourceNotFoundException.class,
            () -> service.getCustomerData(request, responseObserver));
    }
}