package ru.zinovievbank.customerservice.dto;

import static ru.zinovievbank.customerservice.util.RegexUtil.LOGIN_REGEX;

import jakarta.validation.constraints.NotNull;
import ru.zinovievbank.customerservice.util.ForceStringDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequestDto {

    @NotBlank
    @Pattern(regexp = LOGIN_REGEX)
    @JsonProperty("login")
    @JsonDeserialize(using = ForceStringDeserializer.class)
    private String login;

    @NotNull
    @JsonProperty("password")
    private byte[] password;

    @NotNull
    @JsonProperty("type")
    private TypeEnum type;

    @NotNull
    public enum TypeEnum {
        IDENTITY_DOC_NUMBER("IDENTITY_DOC_NUMBER"),
        MOBILE_PHONE("MOBILE_PHONE"),
        INVALID("INVALID");

        private final String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
