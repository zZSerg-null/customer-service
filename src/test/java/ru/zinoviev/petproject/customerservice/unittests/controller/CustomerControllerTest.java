package ru.customer.petproject.customerservice.unittests.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest extends AbstractControllerTest {

    private static final String SECURITY_QUESTION = "Random question";
    private static final String SECURITY_ANSWER = "Random answer";

    @Test
    @DisplayName("CRS-8: should return customerInfoDto when getCustomerInfo " +
            "is called with valid customerId")
    void getCustomerInfo() throws Exception {
        CustomerInfoDto customerInfoDto = CustomerInfoDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .patronymic(PATRONYMIC)
                .birthDate(BIRTH_DAY)
                .mobilePhone(MOBILE_PHONE)
                .email(EMAIL)
                .customerStatus(CustomerStatus.CUSTOMER_NOT_REGISTERED.getCode())
                .build();

//        String request = Jackson2ObjectMapperBuilder.json()
//                .modules(new JavaTimeModule())
//                .build()
//                .writeValueAsString(customerInfoDto);

        String expectedJson = getJsonFromDto(customerInfoDto);

        when(customerService.getCustomerInfoById(anyString())).thenReturn(expectedJson);

        mockMvc.perform(
                        get("/auth/information")
                                .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON)) // text/plain;charset=UTF-8
                .andExpect(content().json(expectedJson, true));
    }

    @Test
    @DisplayName("CRS-9: should update email and return 200 OK")
    void updateEmail() throws Exception {
        String newEmail = "random@email.com";
        EmailDto emailDto = new EmailDto(newEmail);

        mockMvc.perform(
                        patch("/auth/user/settings/email")
                                .header("Authorization", "Bearer " + refreshToken)
                                .contentType(APPLICATION_JSON)
                                .content(getJsonFromDto(emailDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<EmailDto> dtoCapture =
                ArgumentCaptor.forClass(EmailDto.class);

        verify(customerService, times(1))
                .updateEmail(anyString(), dtoCapture.capture());
        assertEquals(newEmail, dtoCapture.getValue().email());
    }

    @Test
    @DisplayName("CRS-15: should update security question and security answer")
    void updateSecurityQuestionAndAnswer() throws Exception {
        String expectedJson = getJsonFromDto(
                new SecurityQuestionAndAnswerDto(SECURITY_QUESTION, SECURITY_ANSWER));

        mockMvc.perform(patch("/auth/user/settings/controls", CUSTOMER_ID)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + refreshToken)
                        .content(expectedJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedJson, true));

        ArgumentCaptor<SecurityQuestionAndAnswerDto> dtoCapture =
                ArgumentCaptor.forClass(SecurityQuestionAndAnswerDto.class);

        verify(customerService, times(1))
                .updateSecurityQuestionAndAnswer(anyString(), dtoCapture.capture());
        assertEquals(SECURITY_QUESTION, dtoCapture.getValue().securityQuestion());
        assertEquals(SECURITY_ANSWER, dtoCapture.getValue().securityAnswer());
    }

    @Test
    @DisplayName("Checking customer by phone number and return 200 OK")
    void checkingCustomer_OK() throws Exception {

        mockMvc.perform(
                get("/auth/checking/" + MOBILE_PHONE))
            .andExpect(status().isOk());

        verify(customerService, times(1))
            .checkCustomerByPhoneNumber(anyString());
    }

    @Test
    @DisplayName("Checking customer by phone number and return 404 OK")
    void checkingCustomer_notFound() throws Exception {

        doThrow(new AppException(EnumException.NOT_FOUND))
            .when(customerService).checkCustomerByPhoneNumber(anyString());

        mockMvc.perform(
                get("/auth/checking/" + MOBILE_PHONE))
            .andExpect(status().isNotFound());

        verify(customerService, times(1))
            .checkCustomerByPhoneNumber(anyString());
    }
}