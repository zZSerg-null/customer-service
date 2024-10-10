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
public class AuthenticationMessageDto {

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("timeStamp")
    private String timeStamp;
}

