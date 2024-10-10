package ru.customer.petproject.customerservice.unittests.util;

import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;

import java.time.LocalDate;
import java.util.UUID;

public class TestKafkaConstants {

    public static final KafkaCustomerDto KAFKA_CUSTOMER_DTO =
            KafkaCustomerDto.builder()
                    .customerUuid(UUID.randomUUID())
                    .passport(KafkaCustomerDto.KafkaPassportDto.builder()
                            .citizenship("РФ")
                            .series("5050")
                            .number("123456")
                            .dateOfIssue(LocalDate.of(2020, 1, 1))
                            .departmentCode("123-456")
                            .issuedBy("Отделением УФМС России по г. Кисловодск")
                            .build())
                    .firstName("First")
                    .lastName("Last")
                    .patronymic("Patronymic")
                    .mobilePhone("79738745798")
                    .email("test@example.com")
                    .accessionDate(LocalDate.now())
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .inArchive(false)
                    .codeword("Codeword")
                    .smsNotification(true)
                    .pushNotification(true)
                    .emailSubscription(true)
                    .inn("123456789012")
                    .sex("Male")
                    .maritalStatus("NEVER_MARRIED")
                    .clientStatus("CLIENT_SDBO")
                    .securityQuestion("Question")
                    .securityAnswer("Answer")
                    .officeRegUuid(UUID.randomUUID())
                    .address(KafkaCustomerDto.KafkaAddressDto.builder()
                            .country("Country")
                            .region("Region")
                            .city("City")
                            .street("Street")
                            .houseNumber("1")
                            .entranceNumber(1)
                            .apartmentNumber(101)
                            .postcode(123456)
                            .oktmo("OKTMO")
                            .build())
                    .build();

    public static final KafkaUpdateCustomerDto KAFKA_UPDATE_CUSTOMER_DTO =
            KafkaUpdateCustomerDto.builder()
                    .customerUuid(UUID.fromString("d44cfcb5-5263-4cf2-a7a3-03ea73359aa1"))
                    .passport(KafkaUpdateCustomerDto.KafkaPassportDto.builder()
                            .number("654321")
                            .build())
                    .firstName("UpFirst")
                    .lastName("UpLast")
                    .patronymic("UpPatronymic")
                    .email("updated_test@example.com")
                    .securityAnswer("Updated Security Answer")
                    .officeRegUuid(UUID.randomUUID())
                    .address(KafkaUpdateCustomerDto.KafkaUpdateAddressDto.builder()
                            .country("UpdatedCountry")
                            .oktmo("NewOKTMO")
                            .build())
                    .build();

}
