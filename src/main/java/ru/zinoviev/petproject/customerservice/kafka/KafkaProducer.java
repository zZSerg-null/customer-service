package ru.zinovievbank.customerservice.kafka;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import ru.zinovievbank.customerservice.dto.kafka.KafkaNotificationDto;
import ru.zinovievbank.customerservice.util.enums.NotificationType;

@Service
@Slf4j
public class KafkaProducer {

    @Value("${spring.kafka.producer.topics.topic-name}")
    private String topicName;

    private final KafkaTemplate<String, KafkaNotificationDto> kafkaTemplate;
    private final Map<NotificationType, Integer> partitionMap;

    public KafkaProducer(KafkaTemplate<String, KafkaNotificationDto> kafkaTemplate,
        Map<NotificationType, Integer> partitionMap) {
        this.kafkaTemplate = kafkaTemplate;
        this.partitionMap = partitionMap;
    }

    public void sendMessage(NotificationType notificationType,
        KafkaNotificationDto kafkaNotificationDto) {
        Integer partition = partitionMap.get(notificationType);

        Message<KafkaNotificationDto> message = MessageBuilder.withPayload(kafkaNotificationDto)
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .setHeader(KafkaHeaders.PARTITION, partition)
            .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
            .build();

        kafkaTemplate.send(message)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Message sent: {} on partition {} with offset {}",
                        kafkaNotificationDto, result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.info("Unable to send message: {} due to {}", kafkaNotificationDto,
                        ex.getLocalizedMessage());
                }
            });
    }
}
