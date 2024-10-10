package ru.zinovievbank.customerservice.dto.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
public record KafkaCustomerDto(
        @NotNull UUID customerUuid,
        KafkaPassportDto passport,
        @NotNull String firstName,
        @NotNull String lastName,
        String patronymic,
        @NotBlank String mobilePhone,
        String email,

        @JsonFormat(pattern = "dd.MM.yyyy")
        @NotNull LocalDate accessionDate,

        @JsonFormat(pattern = "dd.MM.yyyy")
        @NotNull LocalDate birthDate,
        boolean inArchive,
        String codeword,
        boolean smsNotification,
        boolean pushNotification,
        boolean emailSubscription,
        @NotNull String inn,
        @NotNull String sex,
        @NotNull String maritalStatus,
        @NotNull String clientStatus,
        @NotNull String securityQuestion,
        @NotNull String securityAnswer,
        @NotNull UUID officeRegUuid,
        KafkaAddressDto address) implements Serializable {

        /**
         * DTO for {@link Address}
         */
        @Builder
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record KafkaAddressDto(
                @NotNull String country,
                String region,
                @NotNull String city,
                @NotNull String street,
                @NotNull String houseNumber,
                Integer entranceNumber,
                Integer apartmentNumber,
                Integer postcode,
                String oktmo) implements Serializable {
        }

        /**
         * DTO for {@link Passport}
         */
        @Builder
        public record KafkaPassportDto(
                @NotBlank String citizenship,
                @NotBlank String series,
                @NotBlank String number,
                @JsonFormat(pattern = "dd.MM.yyyy")
                LocalDate dateOfIssue,
                @NotBlank String departmentCode,
                @NotBlank String issuedBy) implements Serializable {
        }
}