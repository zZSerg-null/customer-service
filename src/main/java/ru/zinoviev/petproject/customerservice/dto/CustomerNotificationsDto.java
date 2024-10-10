package ru.zinovievbank.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for notifications
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotificationsDto implements Serializable {

    @Email
    private String email = null;
    @NotNull
    private Boolean smsNotification = false;
    @NotNull
    private Boolean pushNotification = true;
    @NotNull
    private Boolean emailSubscription = false;
}