package ru.zinovievbank.customerservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationStatusDto {

    @NotNull
    private Boolean notificationStatus;
}
