package ru.customer.petproject.customerservice.integrationtests;

import customerservice.CustomerData;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.aston.globalexeptionsspringbootstarter.exception.ResourceNotFoundException;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.service.CustomerDataGrpcService;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

import java.util.Optional;
import java.util.UUID;

import static io.grpc.Status.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class GrpcIntegrationTest extends AbstractIntegrationTest {
    @Qualifier("customerDataGrpcService")
    @Autowired
    private CustomerDataGrpcService grpcService;

    @Autowired
    private CustomerRepository customerRepository;

    @ParameterizedTest
    @CsvSource({
            "CUSTOMER_REGISTERED",
            "CUSTOMER_NOT_REGISTERED",
            "CUSTOMER_BLOCKED"
    })
    @DisplayName("gRPC getCustomerStatus method: when valid status should update customer status")
    void getCustomerStatusWhenValidStatusShouldUpdateStatus(String customerStatus) {
        // given
        String mobilePhone = "79974699104";
        Optional<UUID> optionalCustomerUUID = customerRepository.findCustomerIdByMobilePhone(mobilePhone);

        if(optionalCustomerUUID.isEmpty())
        {
            throw new ResourceNotFoundException();
        }

        // when
        StreamObserver<CustomerData.SendingCustomerStatusResponse> responseObserver = mock(StreamObserver.class);
        CustomerData.SendingCustomerStatusRequest customerStatusRequest = CustomerData.SendingCustomerStatusRequest.newBuilder()
                .setMobilePhone(mobilePhone)
                .setCustomerStatus(customerStatus)
                .build();
        CustomerData.SendingCustomerStatusResponse expectedResponse = CustomerData.SendingCustomerStatusResponse.newBuilder()
                .setMessage("Ok")
                .build();

        grpcService.getCustomerStatus(customerStatusRequest, responseObserver);

        // then
        Optional<CustomerStatus> optionalNewCustomerStatus = customerRepository.findCustomerStatusByMobilePhone(mobilePhone);

        if(optionalNewCustomerStatus.isEmpty())
        {
            throw new ResourceNotFoundException();
        }

        CustomerStatus newCustomerStatus = optionalNewCustomerStatus.get();

        verify(responseObserver, times(1))
                .onNext(expectedResponse);
        verify(responseObserver, times(1))
                .onCompleted();
        assertThat(newCustomerStatus).isEqualTo(CustomerStatus.valueOf(customerStatus));
    }

    @Test
    @DisplayName("gRPC getCustomerStatus method: when customer not found should return ResourceNotFoundException")
    void getCustomerStatusShouldReturnErrorWhenCustomerNotFound() {
        // given
        String mobilePhone = "123456789";
        String customerStatus = CustomerStatus.CUSTOMER_BLOCKED.name();

        // when
        StreamObserver<CustomerData.SendingCustomerStatusResponse> responseObserver = mock(StreamObserver.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            grpcService.getCustomerStatus(
                    CustomerData.SendingCustomerStatusRequest.newBuilder()
                            .setMobilePhone(mobilePhone)
                            .setCustomerStatus(customerStatus)
                            .build(),
                    responseObserver);
        });
    }

    @Test
    @DisplayName("gRPC getCustomerStatus method: when customer status not valid should return IllegalArgumentException")
    void getCustomerStatusShouldReturnErrorWhenCustomerStatusNotValid() {
        // given
        String mobilePhone = "79974699104";
        String customerStatus = "Unknown status";

        // when
        StreamObserver<CustomerData.SendingCustomerStatusResponse> responseObserver = mock(StreamObserver.class);

        assertThrows(IllegalArgumentException.class, () -> {
            grpcService.getCustomerStatus(
                    CustomerData.SendingCustomerStatusRequest.newBuilder()
                            .setMobilePhone(mobilePhone)
                            .setCustomerStatus(customerStatus)
                            .build(),
                    responseObserver);
        });
    }
}
