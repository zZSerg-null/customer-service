package ru.zinovievbank.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangePasswordDto {

    @NotNull
    @JsonProperty("password")
    private byte[] password;

    @NotNull
    @JsonProperty("newPassword")
    private byte[] newPassword;

}
