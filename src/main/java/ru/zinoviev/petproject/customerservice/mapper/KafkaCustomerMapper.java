package ru.zinovievbank.customerservice.mapper;

import org.mapstruct.*;
import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;

import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;


/**
 * Mapping {@link Customer}
 * and make Add and Update mapping
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface KafkaCustomerMapper {

    @Mapping(target = "id", source = "customerUuid")
    @Mapping(target = "customerStatus", source = "clientStatus", qualifiedByName = "CustomerStatus")
    @Mapping(target = "passport", ignore = true)
    @Mapping(target = "address", ignore = true)
    Customer toCustomer(KafkaCustomerDto absDto);

    @Mapping(target = ".", source = "passport")
    @Mapping(target = "issuanceDate", source = "passport.dateOfIssue")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "id", ignore = true)
    Passport toPassport(KafkaCustomerDto absDto);

    @Mapping(target = ".", source = "address")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "id", ignore = true)
    Address toAddress(KafkaCustomerDto absDto);

    @Mapping(target = "id", source = "customerUuid")
    @Mapping(target = "customerStatus", source = "clientStatus", qualifiedByName = "CustomerStatus")
    @Mapping(target = "passport", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "accessionDate", ignore = true)
    void updateCustomer(KafkaUpdateCustomerDto absDto, @MappingTarget Customer customer);

    @Mapping(target = ".", source = "passport")
    @Mapping(target = "issuanceDate", source = "passport.dateOfIssue")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updatePassport(KafkaUpdateCustomerDto absDto, @MappingTarget Passport passport);

    @Mapping(target = ".", source = "address")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateAddress(KafkaUpdateCustomerDto absDto, @MappingTarget Address address);

    @Named("CustomerStatus")
    default CustomerStatus getCustomerStatus(String clientStatus){
        return switch (clientStatus){
            case "CLIENT_SDBO" -> CustomerStatus.CUSTOMER_REGISTERED;
            case "CLIENT_NOT_SDBO" -> CustomerStatus.CUSTOMER_NOT_REGISTERED;
            case "CLIENT_BLOCKED" -> CustomerStatus.CUSTOMER_BLOCKED;
            default -> throw new IllegalArgumentException("Customer status from ABS not define");
        };
    }

}
