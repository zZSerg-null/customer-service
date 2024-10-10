package ru.zinovievbank.customerservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
public class UserProfile {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "password")
    @Size(max = 255)
    private String password;

    @Column(name = "profile_reg_datetime")
    private LocalDateTime profileRegDatetime;

    @Column(name = "last_verification_code", nullable = false)
    @Size(max = 6)
    @NotBlank
    private String lastVerificationCode;

    @Column(name = "last_code_expiration", nullable = false)
    private LocalDateTime lastCodeExpiration;

    @Column(name = "next_attempt_time", nullable = false)
    private LocalDateTime nextAttemptTime;

    @Column(name = "wrong_attempts", nullable = false)
    @NotNull
    private Short wrongAttempts;

    @Column(name = "sms_sent_counter", nullable = false)
    @NotNull
    private int smsSentCounter;
}
