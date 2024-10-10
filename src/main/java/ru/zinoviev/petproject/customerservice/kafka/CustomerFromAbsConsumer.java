package ru.zinovievbank.customerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;
import ru.zinovievbank.customerservice.service.CustomerService;

/*
Kafka consumer for abs-add-new-client topic and abs-update-client-info topic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerFromAbsConsumer {

    private final CustomerService customerService;

    private static final String MESSAGE = "Received message. Topic: '{}': {}";

    @KafkaListener(
      topics = "${spring.kafka.consumer.topics.abs-add-new-client-topic}",
      properties = "${spring.kafka.consumer.types.new-customer}"
    )
    public void consumeAdd(KafkaCustomerDto dto) {

        log.info(MESSAGE, "abs_add-new-client-topic", dto);

        customerService.addNewCustomer(dto);
    }

    @KafkaListener(
      topics = "${spring.kafka.consumer.topics.abs-update-client-info-topic}",
      properties = "${spring.kafka.consumer.types.update-customer}"
    )
    public void consumeUpdate(KafkaUpdateCustomerDto dto) {

        log.info(MESSAGE, "abs_update-client-info-topic", dto);

        customerService.updateCustomer(dto);
    }
}
