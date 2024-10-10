package ru.zinovievbank.customerservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.zinovievbank.customerservice.util.ForceStringDeserializer;
import ru.zinovievbank.customerservice.util.RegexUtil;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MobilePhoneAndVerificationCodeDto {

    @NotNull
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mobilePhone")
    @Pattern(regexp = RegexUtil.MOBILE_PHONE)
    private String mobilePhone;

    @NotNull
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("verificationCode")
    private String verificationCode;
}