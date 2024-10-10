package ru.customer.petproject.customerservice.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.customer.petproject.customerservice.unittests.util.TestKafkaConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

class KafkaConsumerIT extends AbstractIntegrationTest {

    @Autowired
    private KafkaTemplate<?, ?> kafkaTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${spring.kafka.consumer.topics.abs-add-new-client-topic}")
    private String absAddNewClientTopic;

    @Value("${spring.kafka.consumer.topics.abs-update-client-info-topic}")
    private String absUpdateClientTopic;

    private final KafkaCustomerDto kafkaCustomerDto
            = TestKafkaConstants.KAFKA_CUSTOMER_DTO;
    private final KafkaUpdateCustomerDto kafkaUpdateCustomerDto
            = TestKafkaConstants.KAFKA_UPDATE_CUSTOMER_DTO;

    @Test
    void consumeAddTest() {
        sendMessage(kafkaCustomerDto, absAddNewClientTopic);

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Customer> optionalCustomer = customerRepository
                            .findFullCustomerInfoById(kafkaCustomerDto.customerUuid());

                    assertTrue(optionalCustomer.isPresent());

                    optionalCustomer.ifPresent(actualCustomer -> assertAll(
                            () -> assertThat(actualCustomer.getId())
                                    .isEqualTo(kafkaCustomerDto.customerUuid()),
                            () -> assertThat(actualCustomer.getAddress().getCountry())
                                    .isEqualTo(kafkaCustomerDto.address().country()),
                            () -> assertThat(actualCustomer.getPassport().getNumber())
                                    .isEqualTo(kafkaCustomerDto.passport().number())
                    ));
                });
    }

    @Test
    void consumeUpdateTest() {
        Optional<Customer> oldOptionalCustomer = customerRepository
                .findFullCustomerInfoById(kafkaUpdateCustomerDto.customerUuid());

        assertTrue(oldOptionalCustomer.isPresent());

        oldOptionalCustomer.ifPresent(oldCustomer -> assertAll(
                () -> assertThat(kafkaUpdateCustomerDto.customerUuid())
                        .isEqualTo(oldCustomer.getId()),
                () -> assertThat(kafkaUpdateCustomerDto.address().country())
                        .isNotEqualTo(oldCustomer.getAddress().getCountry()),
                () -> assertThat(kafkaUpdateCustomerDto.passport().number())
                        .isNotEqualTo(oldCustomer.getPassport().getNumber())
        ));

        sendMessage(kafkaUpdateCustomerDto, absUpdateClientTopic);

        await().pollInterval(Duration.ofSeconds(3)).atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Customer> optionalCustomer = customerRepository
                            .findFullCustomerInfoById(kafkaUpdateCustomerDto.customerUuid());

                    assertTrue(optionalCustomer.isPresent());

                    optionalCustomer.ifPresent(actualCustomer -> assertAll(
                            () -> assertThat(actualCustomer.getId())
                                    .isEqualTo(kafkaUpdateCustomerDto.customerUuid()),
                            () -> assertThat(actualCustomer.getAddress().getCountry())
                                    .isEqualTo(kafkaUpdateCustomerDto.address().country()),
                            () -> assertThat(actualCustomer.getPassport().getNumber())
                                    .isEqualTo(kafkaUpdateCustomerDto.passport().number())
                    ));
                });
    }

    private <T> void sendMessage(T dto, String topic) {
        Objects.requireNonNull(dto);

        Message<T> message =
                MessageBuilder.withPayload(dto)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .setHeader("message_created", LocalDateTime.now())
                        .setHeader("message_lifetime", "86400000")
                        .build();

        kafkaTemplate.send(message);
    }
}
