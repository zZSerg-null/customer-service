package ru.customer.petproject.customerservice.unittests.service.testdata;

import static java.time.LocalDateTime.now;

import java.util.UUID;
import ru.zinovievbank.customerservice.dto.MobilePhoneReceiverDto;
import ru.zinovievbank.customerservice.model.UserProfile;

public class SecurityServiceTestData {
    public static final String PHONE_NUMBER = "79974699104";

    public static final UUID RANDOM_CUSTOMER_ID = UUID.randomUUID();

    public static final MobilePhoneReceiverDto MOBILE_PHONE_RECEIVER_DTO = MobilePhoneReceiverDto.builder()
            .mobilePhone(PHONE_NUMBER)
            .build();

    public static final UserProfile USER_PROFILE = UserProfile.builder()
            .customerId(RANDOM_CUSTOMER_ID)
            .profileRegDatetime(now())
            .lastVerificationCode("123321")
            .lastCodeExpiration(now().plusMinutes(10))
            .nextAttemptTime(now().plusSeconds(30))
            .wrongAttempts((short) 0)
            .smsSentCounter((short) 1)
            .build();
}