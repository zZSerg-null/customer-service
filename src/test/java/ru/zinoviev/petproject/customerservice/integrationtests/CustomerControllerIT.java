package ru.customer.petproject.customerservice.integrationtests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static ru.zinovievbank.customerservice.util.enums.CustomerStatus.CUSTOMER_REGISTERED;

class CustomerControllerIT extends AbstractIntegrationTest {



    private final String MAIN_PATH = "/customer/api/v1";
    private final UUID CUSTOMER_ID = UUID.fromString("6733729f-d627-4122-baab-8100d7242f30");
    private final String FIRST_NAME = "Александр";
    private final String LAST_NAME = "Иванов";
    private final String PATRONYMIC = "Сергеевич";
    private final String BIRTH_DATE = "1981-12-02";
    private final String MOBILE_PHONE = "79101464757";

    private final String INVALID_PHONE = "71111111";
    private final String EMAIL = "aleksandr.ivanov789@yahoo.com";
    private final String CUSTOMER_STATUS = CUSTOMER_REGISTERED.getCode();
    private final String SECURITY_QUESTION = "Что изучает механика?";
    private final String SECURITY_ANSWER = "Движение";

    @Test
    @DisplayName("CRS-8: get customer info when customer id is valid")
    void getCustomerInfo_IfCustomerIdIsValid() {
        final String REFRESH_TOKEN = generateAccessToken(CUSTOMER_ID);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("")
                .body("")
                .body("")
                .body("")
                .header("Authorization", "Bearer " + REFRESH_TOKEN)
                .when()
                .get(MAIN_PATH + "/user/information")
                .then()
                .statusCode(200)
                .body("firstName", equalTo(FIRST_NAME))
                .body("lastName", equalTo(LAST_NAME))
                .body("patronymic", equalTo(PATRONYMIC))
                .body("birthDate", equalTo(BIRTH_DATE))
                .body("mobilePhone", equalTo(MOBILE_PHONE))
                .body("email", equalTo(EMAIL))
                .body("customerStatus", equalTo(CUSTOMER_STATUS));
    }

    @Test
    @DisplayName("CRS-9: update email and return 200 http status ")
    void updateEmail_ShouldReturn_OK() {
        final String REFRESH_TOKEN = generateAccessToken(CUSTOMER_ID);

        String newEmail = "alisanew.morozova456@yahoo.com";
        EmailDto emailDto = new EmailDto(newEmail);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + REFRESH_TOKEN)
                .body(emailDto)
                .when()
                .patch(MAIN_PATH + "/user/settings/email")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("CRS-15: should update security question and security answer")
    void updateSecurityQuestionAndAnswer_ShouldReturn_OK() {
        final String REFRESH_TOKEN = generateAccessToken(CUSTOMER_ID);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + REFRESH_TOKEN)
                .body(new SecurityQuestionAndAnswerDto(SECURITY_QUESTION, SECURITY_ANSWER))
                .when()
                .patch(MAIN_PATH + "/user/settings/controls")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Checking customer when phone number is found in BD")
    void checking_Customer_By_Phone_Number_OK() {

        given()
                .when()
                .get(MAIN_PATH + "/user/checking/" + MOBILE_PHONE)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Checking customer when phone number is not found in BD")
    void checking_Customer_By_Phone_Number_NotFound() {

        given()
                .when()
                .get(MAIN_PATH + "/user/checking/" + INVALID_PHONE)
                .then()
                .statusCode(400);
    }


}
