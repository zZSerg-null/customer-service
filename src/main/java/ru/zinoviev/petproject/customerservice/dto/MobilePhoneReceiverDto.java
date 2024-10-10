package ru.zinovievbank.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.zinovievbank.customerservice.util.ForceStringDeserializer;
import ru.zinovievbank.customerservice.util.RegexUtil;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MobilePhoneReceiverDto {

    @Pattern(regexp = RegexUtil.MOBILE_PHONE)
    @NotBlank
    @JsonDeserialize(using = ForceStringDeserializer.class)
    @JsonProperty("mobilePhone")
    private String mobilePhone;

}
