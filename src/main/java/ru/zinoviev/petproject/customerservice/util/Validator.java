package ru.zinovievbank.customerservice.util;

import java.util.regex.Pattern;

public interface Validator {

    boolean validate(String input);

    static Validator mobilePhoneValidator() {
        return input -> {
            var pattern = Pattern.compile(RegexUtil.MOBILE_PHONE);
            var matcher = pattern.matcher(input);
            return matcher.matches();
        };
    }

    static Validator passportNumberValidator() {
        return input -> {
            var pattern = Pattern.compile(RegexUtil.PASSPORT_NUMBER);
            var matcher = pattern.matcher(input);
            return matcher.matches();
        };
    }
}
