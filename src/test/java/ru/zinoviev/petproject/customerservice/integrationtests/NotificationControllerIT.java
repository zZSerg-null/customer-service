package ru.customer.petproject.customerservice.integrationtests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.zinovievbank.customerservice.dto.NotificationStatusDto;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class NotificationControllerIT extends AbstractIntegrationTest {

    private final String MAIN_PATH = "/customer/api/v1/notification";
    private final UUID CUSTOMER_ID = UUID.fromString("6733729f-d627-4122-baab-8100d7242f30");
    private final String EMAIL = "andrey.mikhaylov123@mail.com";
    private final Boolean SMS_NOTIFICATION = Boolean.TRUE;
    private final Boolean PUSH_NOTIFICATION = Boolean.TRUE;
    private final Boolean EMAIL_SUBSCRIPTION = Boolean.TRUE;
    private String token = null;


    @ParameterizedTest
    @ValueSource(strings = {"sms", "push", "email"})
    @DisplayName("CRS-11, 12, 13: test push notification should return OK")
    void setNotificationPush_shouldReturnOk_ifIdIsValid(String pathPostfix) throws Exception {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        NotificationStatusDto statusDto = new NotificationStatusDto();
        statusDto.setNotificationStatus(true);
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(statusDto)
                .when()
                .patch(MAIN_PATH + "/settings/" + pathPostfix)
                .then()
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sms", "push", "email"})
    @DisplayName("CRS-11, 12, 13: test sms notification should return 415 status if notification status is numerical")
    void testNotificationIfEnterNum(String pathPostfix) {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        final String content = "{\"notificationStatus\": 123}";

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(content)
                .when()
                .patch(MAIN_PATH + "/settings/" + pathPostfix)
                .then()
                .statusCode(415);
    }

    @Test
    @DisplayName("CRS-10: should return email and notification settings (display of switchers position)")
    void getCustomerNotificationsSettings_shouldReturnDtoWithNotificationSettings_ifIdIsValid() {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(MAIN_PATH)
                .then()
                .statusCode(200)
                .body("email", equalTo(EMAIL))
                .body("smsNotification", equalTo(SMS_NOTIFICATION))
                .body("pushNotification", equalTo(PUSH_NOTIFICATION))
                .body("emailSubscription", equalTo(EMAIL_SUBSCRIPTION));
    }

    @Test
    @DisplayName("CRS-11: test push notification should return OK")
    void setNotificationPush_shouldReturnOk_ifIdIsValid() {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        NotificationStatusDto statusDto = new NotificationStatusDto();
        statusDto.setNotificationStatus(!PUSH_NOTIFICATION);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(statusDto)
                .when()
                .patch(MAIN_PATH + "/settings/push")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(MAIN_PATH)
                .then()
                .statusCode(200)
                .body("pushNotification", equalTo(!PUSH_NOTIFICATION));
    }

    @Test
    @DisplayName("CRS-12: test sms notification should return OK")
    void setNotificationSms_shouldReturnOk_ifIdIsValid() {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        NotificationStatusDto statusDto = new NotificationStatusDto();
        statusDto.setNotificationStatus(!SMS_NOTIFICATION);

        given()
                .contentType(ContentType.JSON)
                .body(statusDto)
                .header("Authorization", "Bearer " + token)
                .when()
                .patch(MAIN_PATH + "/settings/sms")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(MAIN_PATH)
                .then()
                .statusCode(200)
                .body("smsNotification", equalTo(!SMS_NOTIFICATION));
    }

    @Test
    @DisplayName("CRS-13: test email notification should return OK")
    void setNotificationEmail_shouldReturnOk_ifIdIsValid() {
        if (token == null) {
            token = generateAccessToken(CUSTOMER_ID);
        }

        NotificationStatusDto statusDto = new NotificationStatusDto();
        statusDto.setNotificationStatus(!EMAIL_SUBSCRIPTION);

        given()
                .contentType(ContentType.JSON)
                .body(statusDto)
                .header("Authorization", "Bearer " + token)
                .when()
                .patch(MAIN_PATH + "/settings/email")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(MAIN_PATH)
                .then()
                .statusCode(200)
                .body("emailSubscription", equalTo(!EMAIL_SUBSCRIPTION));
    }
}
