package ru.customer.petproject.customerservice.unittests.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.aston.globalexeptionsspringbootstarter.config.GlobalExceptionHandlerAutoConfiguration;
import ru.zinovievbank.customerservice.controller.CustomerController;
import ru.zinovievbank.customerservice.controller.NotificationController;
import ru.zinovievbank.customerservice.service.CustomerService;
import ru.zinovievbank.customerservice.service.NotificationService;
import ru.zinovievbank.customerservice.auth.JwtProvider;

import java.time.LocalDate;
import java.util.UUID;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        CustomerController.class,
        NotificationController.class
})

@ImportAutoConfiguration(GlobalExceptionHandlerAutoConfiguration.class)
@EnableWebSecurity
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @SpyBean
    protected JwtProvider jwtProvider;

    @MockBean
    protected CustomerService customerService;

    @MockBean
    protected NotificationService notificationService;



    protected static final UUID CUSTOMER_ID = UUID.fromString("6733729f-d627-4122-baab-8100d7242f30");
    protected static final String FIRST_NAME = "Александр";
    protected static final String LAST_NAME = "Grey";
    protected static final String PATRONYMIC = "Patronymic";
    protected static final LocalDate BIRTH_DAY = LocalDate.now().minusYears(25);
    protected static final String MOBILE_PHONE = "71234567890";
    protected static final String EMAIL = "test@test.com";

    protected String refreshToken;

//    @BeforeEach
//    void setupEach() {
//        refreshToken = jwtProvider.generateRefreshToken(CUSTOMER_ID);
//    }

    protected <T> String getJsonFromDto(T dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error in controller test during DTO serialisation" + e.getMessage());
        }
    }
}
