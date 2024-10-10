package ru.customer.petproject.customerservice.integrationtests;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.zinovievbank.customerservice.dto.kafka.KafkaNotificationDto;
import ru.zinovievbank.customerservice.kafka.KafkaProducer;
import ru.zinovievbank.customerservice.util.enums.NotificationType;

class KafkaProducerIT extends AbstractIntegrationTest {

    public static final String TOPIC_NAME_SEND_CLIENT = "customer_service";
    public static final UUID CUSTOMER_ID = UUID.randomUUID();
    public static final NotificationType NOTIFICATION_TYPE = NotificationType.EMAIL;
    public static final Boolean NOTIFICATION_STATUS = true;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    @DisplayName("Kafka should send order event")
    void it_should_send_order_event() {

        KafkaNotificationDto messageDto = new KafkaNotificationDto();
        messageDto.setCustomerId(CUSTOMER_ID.toString());
        messageDto.setNotificationType(NOTIFICATION_TYPE.toString());
        messageDto.setNotificationStatus(NOTIFICATION_STATUS);

        kafkaProducer.sendMessage(NOTIFICATION_TYPE, messageDto);

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-java-test");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, KafkaNotificationDto.class);
        KafkaConsumer<String, KafkaNotificationDto> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(TOPIC_NAME_SEND_CLIENT));
        ConsumerRecords<String, KafkaNotificationDto> records = consumer.poll(Duration.ofMillis(10000L));
        consumer.close();

        Assertions.assertEquals(CUSTOMER_ID.toString(), records.iterator().next().value().getCustomerId());
    }
}
