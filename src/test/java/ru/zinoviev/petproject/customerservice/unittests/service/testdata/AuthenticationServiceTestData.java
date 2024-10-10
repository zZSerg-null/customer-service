package ru.customer.petproject.customerservice.unittests.service.testdata;

import java.time.LocalDateTime;
import java.util.UUID;
import ru.zinovievbank.customerservice.dto.JwtRequestDto;
import ru.zinovievbank.customerservice.dto.JwtRequestDto.TypeEnum;
import ru.zinovievbank.customerservice.dto.RefreshTokenDto;
import ru.zinovievbank.customerservice.model.UserProfile;
import ru.zinovievbank.customerservice.model.UserToken;
import ru.zinovievbank.customerservice.util.CustomPasswordEncoder;


public class AuthenticationServiceTestData {

    public static final String STRING_UUID_CUSTOMER_ID_1 = "80129e92-e26e-4ca8-a158-2752c9db1508";
    public static final UUID CUSTOMER_ID_1 = UUID.fromString(STRING_UUID_CUSTOMER_ID_1);

    public static final String VALID_PASSWORD_USER_1 = "d6539f2430bdccab89f946908e206a53670723b807218b9bb6f42a07c4f4aca6";

    public static final String VALID_PHONE_USER_1 = "78005553535";
    public static final String VALID_PASSPORT_USER_1 = "4954262577";
    public static final String VALID_PHONE_BLOCKED_USER_1 = "79198523955";
    public static final String VALID_PASSPORT_BLOCKED_USER_1 = "7641952138";

    public static final String NO_PHONE_IN_DB = "70000000000";
    public static final String NO_PASSPORT_IN_DB = "0123456789";

    public static final String INVALID_PASSWORD_USER_1 = "bad55f32bfa0921006844d7d87c597c730b21a2a7c42bf849d0cf813cdf0e4ad";
    public static final String INVALID_PASSPORT = "0123456789g";

    public static final String VALUE_REFRESH_TOKEN_OLD_1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String VALUE_REFRESH_TOKEN_NEW_1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxZjZmN2NmMy0xYjFjLTNmNjgtOWUwMC01NWU4ZjMzMWI4OWQiLCJpYXQiOjE2OTUxOTk3MzksImV4cCI6MTY5NTE5OTczOX0.Py8hYUUvmTC9ZTdFcL1DREcJ7sAHJ22sn1Eq73UGGs8";
    public static final String VALUE_REFRESH_TOKEN_INVALID_1 = "yJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
    public static final UserProfile USER_PROFILE_1 = UserProfile.builder()
            .customerId(CUSTOMER_ID_1)
            .password(passwordEncoder.encode(VALID_PASSWORD_USER_1))
            .lastVerificationCode("123456")
            .lastCodeExpiration(LocalDateTime.now())
            .nextAttemptTime(LocalDateTime.now())
            .wrongAttempts((short) 0)
            .smsSentCounter((short) 0)
            .build();

    public static final UserToken REFRESH_TOKEN_USER_1_OLD = UserToken.builder()
            .tokenId(UUID.fromString("2f6f7cf3-1b1c-3f68-9e00-55e8f331b89d"))
            .userProfile(USER_PROFILE_1)
            .tokenValue(VALUE_REFRESH_TOKEN_OLD_1)
            .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_USER_1 = JwtRequestDto.builder()
            .login(VALID_PHONE_USER_1)
            .password(VALID_PASSWORD_USER_1.getBytes())
            .type(JwtRequestDto.TypeEnum.MOBILE_PHONE)
            .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_PASSPORT_USER_1 = JwtRequestDto.builder()
        .login(VALID_PASSPORT_USER_1)
        .password(VALID_PASSWORD_USER_1.getBytes())
        .type(TypeEnum.IDENTITY_DOC_NUMBER)
        .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_NO_PHONE_IN_DB = JwtRequestDto.builder()
            .login(NO_PHONE_IN_DB)
            .password(VALID_PASSWORD_USER_1.getBytes())
            .type(JwtRequestDto.TypeEnum.MOBILE_PHONE)
            .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_NO_PASSPORT_IN_DB = JwtRequestDto.builder()
        .login(NO_PASSPORT_IN_DB)
        .password(VALID_PASSWORD_USER_1.getBytes())
        .type(TypeEnum.IDENTITY_DOC_NUMBER)
        .build();
    public static final JwtRequestDto JWT_REQUEST_DTO_BLOCKED_USER_PASSPORT = JwtRequestDto.builder()
        .login(VALID_PASSPORT_BLOCKED_USER_1)
        .password(VALID_PASSWORD_USER_1.getBytes())
        .type(TypeEnum.IDENTITY_DOC_NUMBER)
        .build();
    public static final JwtRequestDto JWT_REQUEST_DTO_BLOCKED_USER_PHONE = JwtRequestDto.builder()
        .login(VALID_PHONE_BLOCKED_USER_1)
        .password(VALID_PASSWORD_USER_1.getBytes())
        .type(TypeEnum.MOBILE_PHONE)
        .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_INVALID_PASSPORT = JwtRequestDto.builder()
        .login(INVALID_PASSPORT)
        .password(VALID_PASSWORD_USER_1.getBytes())
        .type(TypeEnum.IDENTITY_DOC_NUMBER)
        .build();

    public static final RefreshTokenDto VALUE_REFRESH_TOKEN_OLD = RefreshTokenDto.builder()
            .refreshToken(VALUE_REFRESH_TOKEN_OLD_1)
            .build();

    public static final RefreshTokenDto VALUE_REFRESH_TOKEN_INVALID = RefreshTokenDto.builder()
            .refreshToken(VALUE_REFRESH_TOKEN_INVALID_1)
            .build();

    public static final JwtRequestDto JWT_REQUEST_DTO_USER_1_WITH_INVALID_PASSWORD = JwtRequestDto.builder()
            .login(VALID_PHONE_USER_1)
            .password(INVALID_PASSWORD_USER_1.getBytes())
            .type(JwtRequestDto.TypeEnum.MOBILE_PHONE)
            .build();
}