package ru.zinovievbank.customerservice.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionTokenResponseDto {
    @JsonProperty("sessionToken")
    private String sessionToken;
}