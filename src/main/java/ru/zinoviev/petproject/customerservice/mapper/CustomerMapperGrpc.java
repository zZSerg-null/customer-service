package ru.zinovievbank.customerservice.mapper;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.aston.grpc.server.CustomerResponse;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Component
public class CustomerMapperGrpc {
    public Customer mapFromCustomerResponseToCustomer(CustomerResponse response) {
        Customer customer = new Customer();

        customer.setId(UUID.fromString(response.getCustomerUuid()));
        customer.setFirstName(response.getFirstName());
        customer.setLastName(response.getLastName());
        customer.setPatronymic(response.getPatronymic());
        customer.setMobilePhone(response.getMobilePhone());
        customer.setAccessionDate(mapTimestampToLocalDate(response.getAccesionDate()));
        customer.setEmail(response.getEmail());
        customer.setBirthDate(mapTimestampToLocalDate(response.getBirthDate()));
        customer.setInn(response.getInn());


        customer.setSmsNotification(response.getSmsNotification());
        customer.setPushNotification(response.getPushNotification());
        customer.setEmailSubscription(response.getEmailSubscription());
        customer.setCustomerStatus(mapClientToCustomerStatus(response.getClientStatus()));
        customer.setSecurityQuestion(response.getSecurityQuestion());
        customer.setSecurityAnswer(response.getSecurityAnswer());

        Address address = getAddress(response);
        Passport passport = getPassport(response);
        address.setCustomer(customer);
        passport.setCustomer(customer);
        customer.setAddress(address);
        customer.setPassport(passport);

        return customer;
    }

    private Address getAddress(CustomerResponse response) {
        Address address = new Address();
        address.setCountry(response.getCountry());
        address.setRegion(response.getRegion());
        address.setCity(response.getCity());
        address.setStreet(response.getStreet());
        address.setHouseNumber(response.getHouseNumber());
        address.setEntranceNumber(String.valueOf(response.getEntranceNumber()));
        address.setApartmentNumber(response.getApartmentNumber());
        address.setPostcode(response.getPostcode());
        address.setOktmo(response.getOktmo());

        return address;
    }

    private Passport getPassport(CustomerResponse response) {
        Passport passport = new Passport();
        passport.setIssuanceDate(mapTimestampToLocalDate(response.getDateOfIssue()));
        passport.setIssuedBy(response.getIssuedBy());
        passport.setDepartmentCode(response.getDepartamentCode());
        passport.setCitizenship(response.getCitizenship());
        passport.setSeries(response.getSeries());
        passport.setNumber(response.getNumber());

        return passport;
    }


    private LocalDate mapTimestampToLocalDate(Timestamp timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return instant.atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private CustomerStatus mapClientToCustomerStatus(String customerStatus) {
        return switch (customerStatus) {
            case "CLIENT_NOT_SDBO" -> CustomerStatus.CUSTOMER_NOT_REGISTERED;
            case "CLIENT_SDBO" -> CustomerStatus.CUSTOMER_REGISTERED;
            case "CLIENT_BLOCKED" -> CustomerStatus.CUSTOMER_BLOCKED;
            default -> throw new AppException(EnumException.BAD_REQUEST, "Unknown customer status");
        };

    }
}

