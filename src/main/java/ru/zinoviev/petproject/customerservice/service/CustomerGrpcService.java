package ru.zinovievbank.customerservice.service;

import io.grpc.StatusRuntimeException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.aston.grpc.server.CustomerAvailablePhoneRequest;
import ru.aston.grpc.server.CustomerResponse;
import ru.aston.grpc.server.ServerGrpc;
import ru.zinovievbank.customerservice.mapper.CustomerMapperGrpc;
import ru.zinovievbank.customerservice.model.Customer;

import java.util.Optional;

@Service
@Slf4j
public class CustomerGrpcService {

    @Setter
    @GrpcClient("abs-server")
    private ServerGrpc.ServerBlockingStub serverStub;
    final CustomerMapperGrpc customerMapperGrpc;

    public CustomerGrpcService(CustomerMapperGrpc customerMapperGrpc) {
        this.customerMapperGrpc = customerMapperGrpc;
    }



    public Optional<Customer> getCustomerByMobilePhone(String mobilePhone) {
        CustomerAvailablePhoneRequest request = createRequest(mobilePhone);
        try {
            log.info("Sending request to gRPC service");
            CustomerResponse response = serverStub.getCustomerAvailablePhone(request);
            Customer customer = customerMapperGrpc.mapFromCustomerResponseToCustomer(response);
            return Optional.ofNullable(customer);
        } catch (StatusRuntimeException e) {
            throw new AppException(EnumException.BAD_REQUEST);
        }
    }

    private CustomerAvailablePhoneRequest createRequest(String mobilePhone) {
        return CustomerAvailablePhoneRequest.newBuilder()
                .setMobilePhone(mobilePhone)
                .build();
    }


}
