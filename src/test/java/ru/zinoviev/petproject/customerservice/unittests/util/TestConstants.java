package ru.customer.petproject.customerservice.unittests.util;

import java.util.UUID;
import ru.zinovievbank.customerservice.dto.ChangePasswordDto;
import ru.zinovievbank.customerservice.dto.MobilePhoneAndVerificationCodeDto;

public class TestConstants {

    private TestConstants() {
    }

    // Phone numbers
    public static final String EXISTING_PHONE_NUMBER = "77333027601";
    public static final String EXISTING_PHONE_NUMBER_FROM_DB = "79550264913";
    public static final String EXISTING_PHONE_NUMBER_FROM_DB_TO_BLOCK = "79192414157";
    public static final String NON_EXISTING_PHONE_NUMBER = "78925817478";
    public static final String INVALID_PHONE_NUMBER = "789258asd17478 ";

    // Verification codes
    public static final String VALID_CODE = "123456";
    public static final String VALID_CODE_FROM_DB = "900258";
    public static final String INVALID_CODE = "000000";

    // Customer ids
    public static final UUID CUSTOMER_ID_NORMAL = UUID.fromString("d54eb158-7499-4bda-bafb-d4bd965a1985");
    public static final UUID CUSTOMER_ID_NORMAL_FROM_DB = UUID.fromString("d44cfcb5-5263-4cf2-a7a3-03ea73359aa1");
    public static final UUID CUSTOMER_ID_WITH_NO_SMS_SENT = UUID.fromString("8841633b-a7a4-472a-b64b-d366d1862951");
    public static final UUID CUSTOMER_ID_WITH_CODE_EXPIRATION = UUID.fromString("f1883f13-834b-4690-a603-f5afcf1c6ea3");
    public static final UUID CUSTOMER_ID_WITH_INVALID_CODE = UUID.fromString("d7ceee94-1453-4391-972e-64abf870d42b");
    public static final UUID CUSTOMER_ID_WITH_INVALID_CODE_AND_LIMIT_ATTEMPTS = UUID.fromString("c53bc047-a295-40ca-acbf-90b86a3bffa9");
    public static final UUID CUSTOMER_ID_WITH_NON_EXISTENT_USER_PROFILE = UUID.fromString("875eb0dc-f6eb-4acb-9e26-690ea3444e5b");

    // Passwords
    public static final String NEW_PASSWORD = "MDM3OGVlYzk3NTFkMmFmYWNjMDQxYWIzNDI0MmE2MmQxZjY4OTVkMjk1OTEyZjZiYTZkZDllY2RiNzJmMmIwNA==";
    public static final String ENCODE_NEW_PASSWORD = "d6539f2430bdccab89f946908e206a53670723b807218b9bb6f42a07c4f4aca6";
    public static final String OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL = "6a596b1b644b4d6e2f44733b074e612f6e1e704825651e6175193650245f7000";
    public static final String ENCODE_OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL = "d6539f2430bdccab89f946908e206a53670723b807218b9bb6f42a07c4f4aca7";
    public static final String ENCODE_PASSWORD_FOR_CUSTOMER_ID_NORMAL = "364e114f62454b1074702a66734d546e4c32382a2a055b7a3e660f07065a190e";
    public static final String INVALID_OLD_PASSWORD = "6a596b1b644b4d6e2f44733b074e612f6e1e704825651e6175193650245f7001";
    public static final String ENCODE_INVALID_OLD_PASSWORD = "d6539f2430bdccab89f946908e206a53670723b807218b9bb6f42a07c4f4aca5";
    public static final String NEW_PASSWORD_NOT_BASE64_FORMAT = "invalidFormatPassword";

    // Time expiration
    public static final long TEN_MINUTES_IN_MILLISECONDS = 600000;

    // Error messages
    public static final String ERROR_MESSAGE_WITH_WRONG_PHONE_NUMBER = "Customer id not found";
    public static final String ERROR_MESSAGE_WITH_NOT_CORRECT_PHONE_NUMBER = "Phone not correct";
    public static final String ERROR_MESSAGE_WITH_NO_SMS_SENT = "No SMS sent";
    public static final String ERROR_MESSAGE_WITH_CODE_EXPIRATION = "The code has expired";
    public static final String ERROR_MESSAGE_WITH_INVALID_CODE = "The code is invalid, please try again";
    public static final String ERROR_MESSAGE_WITH_INVALID_CODE_AND_LIMIT_ATTEMPTS = "The code is invalid. The limit of attempts has been reached, please request the code again";
    public static final String ERROR_MESSAGE_WITH_NON_EXISTENT_USER_PROFILE = "UserProfile not found";
    public static final String ERROR_MESSAGE_WITH_INCORRECT_ENCODE_PASSWORD = "Incorrect password encode in Base64";
    public static final String ERROR_MESSAGE_WITH_ALREADY_HAVE_PASSWORD = "User already have password";

    // Tokens
    public static final String REFRESH_TOKEN = "Bearer refresh";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDFkZWZiYy1hZTk4LTNkZjMtODJlYy1hOThhMjMxMjEwNzIiLCJpYXQiOjE2OTcxODE4NjAsImV4cCI6MTY5NzE4MzY2MH0.HXO-X31WHNCpCeg4x1oDjbp3Potc98dMf3vYpPzJa2Q";
    public static final String ERROR_MESSAGE_JSON_PATH = "message";

    // DTOs
    public static final MobilePhoneAndVerificationCodeDto REQUEST_DTO_WITH_EXISTING_PHONE_NUMBER_AND_VALID_CODE =
            MobilePhoneAndVerificationCodeDto.builder()
                    .mobilePhone(EXISTING_PHONE_NUMBER)
                    .verificationCode(VALID_CODE)
                    .build();
    public static final MobilePhoneAndVerificationCodeDto REQUEST_DTO_WITH_INVALID_PHONE_NUMBER_AND_VALID_CODE =
            MobilePhoneAndVerificationCodeDto.builder()
                    .mobilePhone(INVALID_PHONE_NUMBER)
                    .verificationCode(VALID_CODE)
                    .build();
    public static final MobilePhoneAndVerificationCodeDto REQUEST_DTO_WITH_EXISTING_PHONE_NUMBER_AND_INVALID_CODE =
            MobilePhoneAndVerificationCodeDto.builder()
                    .mobilePhone(EXISTING_PHONE_NUMBER)
                    .verificationCode(INVALID_CODE)
                    .build();
    public static final MobilePhoneAndVerificationCodeDto REQUEST_DTO_WITH_EXISTING_PHONE_NUMBER =
            MobilePhoneAndVerificationCodeDto.builder()
                    .mobilePhone(EXISTING_PHONE_NUMBER)
                    .build();
    public static final MobilePhoneAndVerificationCodeDto REQUEST_DTO_WITH_NON_EXISTING_PHONE_NUMBER =
            MobilePhoneAndVerificationCodeDto.builder()
                    .mobilePhone(NON_EXISTING_PHONE_NUMBER)
                    .build();

    public static final ChangePasswordDto CHANGE_PASSWORD_DTO =
        ChangePasswordDto.builder()
            .password(ENCODE_OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL.getBytes())
            .newPassword(ENCODE_NEW_PASSWORD.getBytes())
            .build();

    public static final ChangePasswordDto CHANGE_PASSWORD_DTO_WITH_EXPIRED_TOKEN =
        ChangePasswordDto.builder()
            .password(ENCODE_OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL.getBytes())
            .newPassword(NEW_PASSWORD.getBytes())
            .build();

    public static final ChangePasswordDto CHANGE_PASSWORD_DTO_WITH_INVALID_OLD_PASSWORD =
        ChangePasswordDto.builder()
            .password(ENCODE_INVALID_OLD_PASSWORD.getBytes())
            .newPassword(NEW_PASSWORD.getBytes())
            .build();

    public static final ChangePasswordDto CHANGE_PASSWORD_DTO_WITH_INVALID_FORMAT_OF_NEW_PASSWORD =
        ChangePasswordDto.builder()
            .password(OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL.getBytes())
            .newPassword(NEW_PASSWORD_NOT_BASE64_FORMAT.getBytes())
            .build();

    public static final ChangePasswordDto CHANGE_PASSWORD_DTO_FOR_NOT_EXISTING_USER =
        ChangePasswordDto.builder()
            .password(ENCODE_OLD_PASSWORD_FOR_CUSTOMER_ID_NORMAL.getBytes())
            .newPassword(ENCODE_NEW_PASSWORD.getBytes())
            .build();
}
