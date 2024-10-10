package ru.zinovievbank.customerservice.service;

import static customerservice.CustomerData.CustomerDataRequest;
import static customerservice.CustomerData.CustomerDataResponse;
import static customerservice.CustomerServiceGrpcGrpc.CustomerServiceGrpcImplBase;
import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;

import com.google.protobuf.Timestamp;
import customerservice.CustomerData;
import io.grpc.stub.StreamObserver;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import jakarta.websocket.OnClose;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.globalexeptionsspringbootstarter.exception.ResourceNotFoundException;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@Slf4j
@GrpcService
@Transactional
public class CustomerDataGrpcService extends CustomerServiceGrpcImplBase {

    private final CustomerRepository customerRepository;

    public CustomerDataGrpcService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void getCustomerData(CustomerDataRequest request,
        StreamObserver<CustomerDataResponse> responseObserver) {
        String customerId = request.getCustomerId();
        Customer customer;
        Address address;
        Passport passport;

        Optional<Customer> optionalCustomer;
        if (!isUUID(customerId)) {
            throw new ResourceNotFoundException();
        }
        optionalCustomer = customerRepository.findFullCustomerInfoById(UUID.fromString(customerId));
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        customer = optionalCustomer.get();
        address = optionalCustomer.get().getAddress();
        passport = customer.getPassport();

        var customerAddress = CustomerData.Address.newBuilder()
            .setCountry(address.getCountry())
            .setRegion(address.getRegion())
            .setCity(address.getCity())
            .setStreet(address.getStreet())
            .setHouseNumber(address.getHouseNumber())
            .setEntranceNumber(address.getEntranceNumber())
            .setApartmentNumber(address.getApartmentNumber().toString())
            .setPostcode(address.getPostcode().toString())
            .setOktmo(address.getOktmo())
            .build();

        var birthdate = Timestamp.newBuilder()
            .setSeconds(customer.getBirthDate().atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .getEpochSecond())
            .setNanos(0)
            .build();

        var issuanceDate = Timestamp.newBuilder()
            .setSeconds(customer.getPassport()
                .getIssuanceDate()
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .getEpochSecond())
            .setNanos(0)
            .build();

        var customerPassport = CustomerData.Passport.newBuilder()
            .setCitizenship(passport.getCitizenship())
            .setSeries(passport.getSeries())
            .setNumber(passport.getNumber())
            .setIssuanceDate(issuanceDate)
            .setIssuedBy(passport.getIssuedBy())
            .setDepartmentCode(passport.getDepartmentCode())
            .build();

        var response = CustomerDataResponse.newBuilder()
            .setCustomerId(customer.getId().toString())
            .setFirstName(customer.getFirstName())
            .setLastName(customer.getLastName())
            .setPatronymic(customer.getPatronymic())
            .setBirthdate(birthdate)
            .setInn(customer.getInn())
            .setMobilePhone(customer.getMobilePhone())
            .setEmail(customer.getEmail())
            .setSmsNotification(customer.getSmsNotification())
            .setPushNotification(customer.getPushNotification())
            .setEmailNotification(customer.getEmailSubscription())
            .setPassport(customerPassport)
            .setAddress(customerAddress)
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private boolean isUUID(String customerId) {
        try {
            UUID.fromString(customerId);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void getCustomerStatus(CustomerData.SendingCustomerStatusRequest request,
        StreamObserver<CustomerData.SendingCustomerStatusResponse> responseObserver) {

        String mobilePhone = request.getMobilePhone();
        String customerStatus = request.getCustomerStatus();

        log.info("Save new customer status received by gRPC - mobile phone: " + mobilePhone + ", customer status: " + customerStatus);

        Optional<UUID> optionalCustomerUUID = customerRepository.findCustomerIdByMobilePhone(mobilePhone);

        if(optionalCustomerUUID.isEmpty())
        {
            responseObserver.onError(NOT_FOUND.withDescription("Customer not found").asRuntimeException());
            throw new ResourceNotFoundException();
        }

        UUID customerUUID = optionalCustomerUUID.get();

        switch (customerStatus) {
            case "CUSTOMER_BLOCKED":
                customerRepository.updateCustomerById(CustomerStatus.CUSTOMER_BLOCKED, customerUUID);
                break;
            case "CUSTOMER_NOT_REGISTERED":
                customerRepository.updateCustomerById(CustomerStatus.CUSTOMER_NOT_REGISTERED, customerUUID);
                break;
            case "CUSTOMER_REGISTERED":
                customerRepository.updateCustomerById(CustomerStatus.CUSTOMER_REGISTERED, customerUUID);
                break;
            default:
                responseObserver.onError(INVALID_ARGUMENT.withDescription("Invalid customer status").asRuntimeException());
                throw new IllegalArgumentException("Invalid customer status: " + customerStatus);
        }

        responseObserver.onNext(CustomerData.SendingCustomerStatusResponse.newBuilder().setMessage("Ok").build());
        responseObserver.onCompleted();
    }
}