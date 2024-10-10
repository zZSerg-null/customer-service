package ru.zinovievbank.customerservice.dto.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;

/**
 * DTO for {@link Customer}
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record KafkaUpdateCustomerDto(
        @NotNull UUID customerUuid,
        KafkaPassportDto passport,
        String firstName,
        String lastName,
        String patronymic,
        String mobilePhone,
        String email,
        @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthDate,
        Boolean inArchive,
        String codeword,
        Boolean smsNotification,
        Boolean pushNotification,
        Boolean emailSubscription,
        String inn,
        String sex,
        String maritalStatus,
        String clientStatus,
        String securityQuestion,
        String securityAnswer,
        @NotNull UUID officeRegUuid,
        KafkaUpdateAddressDto address) implements Serializable {

    /**
     * DTO for {@link Address}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record KafkaUpdateAddressDto(
            String country,
            String region,
            String city,
            String street,
            String houseNumber,
            Integer entranceNumber,
            Integer apartmentNumber,
            Integer postcode,
            String oktmo) implements Serializable {
    }

    /**
     * DTO for {@link Passport}
     */
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record KafkaPassportDto(
            String citizenship,
            String series,
            String number,
            @JsonFormat(pattern = "dd.MM.yyyy")
            LocalDate dateOfIssue,
            String departmentCode,
            String issuedBy) implements Serializable {
    }
}