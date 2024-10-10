package ru.customer.petproject.customerservice.unittests.util;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.zinovievbank.customerservice.util.RegexUtil;

class RegexUtilTest {

    @ParameterizedTest(name = "{index} - {0} is match regex")
    @ValueSource(strings = {
        "vasyapupkin@gmail.com", "example@domain.com", "MAKENZIE151@yahoo.com",
        "exaMple123@domain.com",
        "exa123mple@domain.com", "exa325mp.le@domain.com", "ex.ample@domain.com"})
    @DisplayName("Email regex is valid")
    void emailRegexIsValid(String email) {
        var pattern = Pattern.compile(RegexUtil.EMAIL);
        var matcher = pattern.matcher(email);
        Assertions.assertTrue(matcher.matches());
    }

    @ParameterizedTest(name = "{index} - {0} is match regex")
    @ValueSource(strings = {
        "a@mail.ru", "exam..ple@domain.com", ".MAKENZIE151@yahoo.com",
        "exaMple123.@domain.com", "exa325mp.le@domain.1com",
            "exa325mp.le@domain.-com"
    })
    @DisplayName("Email regex is invalid")
    void emailRegexIsInvalid(String email) {
        var pattern = Pattern.compile(RegexUtil.EMAIL);
        var matcher = pattern.matcher(email);
        Assertions.assertFalse(matcher.matches());
    }

}