package ru.zinovievbank.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    @JsonProperty("refreshToken")
    private String refreshToken;

}
