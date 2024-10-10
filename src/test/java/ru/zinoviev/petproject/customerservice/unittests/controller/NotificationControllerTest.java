package ru.customer.petproject.customerservice.unittests.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends AbstractControllerTest {

    private static final Boolean SMS_NOTIFICATION = Boolean.FALSE;
    private static final Boolean PUSH_NOTIFICATION = Boolean.TRUE;
    private static final Boolean EMAIL_SUBSCRIPTION = Boolean.FALSE;

    @ParameterizedTest
    @ValueSource(strings = {"sms", "push", "email"})
    @DisplayName("CRS-11, 12, 13: test push notification should return OK")
    void setNotificationPush_shouldReturnOk_ifIdIsValid(String pathPostfix) throws Exception {
        String body = getJsonFromDto(new NotificationStatusDto(true));

        mockMvc.perform(
                        patch("/notifications/settings/" + pathPostfix)
                                .header("Authorization", "Bearer " + refreshToken)
                                .contentType(APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sms", "push", "email"})
    @DisplayName("CRS-11, 12, 13: test push notification should return 401 status")
    void setNotificationPush_shouldReturn_Unauthorized(String pathPostfix) throws Exception {
        String body = getJsonFromDto(new NotificationStatusDto(true));

        mockMvc.perform(
                        patch("/notifications/settings/" + pathPostfix)
                                .header("Authorization", "Bearer " + "bad Token")
                                .contentType(APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sms", "push", "email"})
    @DisplayName("CRS-11, 12, 13: test sms notification should return 415 status " +
            "if notification status is numerical")
    void testNotificationIfEnterNum(String pathPostfix) throws Exception {
        String body = "{\"notificationStatus\": 123}";

        mockMvc.perform(
                        patch("/notifications/settings/" + pathPostfix)
                                .header("Authorization", "Bearer " + refreshToken)
                                .contentType(APPLICATION_JSON)
                                .content(body))
                .andExpect(status().is(415));
    }

    @Test
    @DisplayName("CRS-10: test notifications settings should return email and " +
            "notification settings (display of switchers position)")
    void testNotificationsSettings() throws Exception {
        CustomerNotificationsDto customerNotificationsDto =
                new CustomerNotificationsDto(EMAIL, SMS_NOTIFICATION, PUSH_NOTIFICATION, EMAIL_SUBSCRIPTION);
        String expectedJson = getJsonFromDto(customerNotificationsDto);

        when(notificationService.getCustomerNotificationsSettings(anyString()))
                .thenReturn(customerNotificationsDto);

        mockMvc.perform(
                        get("/notifications")
                                .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        verify(notificationService, times(1))
                .getCustomerNotificationsSettings(anyString());
    }
}