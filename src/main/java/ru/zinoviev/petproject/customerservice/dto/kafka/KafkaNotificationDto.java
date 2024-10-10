package ru.zinovievbank.customerservice.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaNotificationDto {

    private String customerId;
    private String notificationType;
    private Boolean notificationStatus;
}
