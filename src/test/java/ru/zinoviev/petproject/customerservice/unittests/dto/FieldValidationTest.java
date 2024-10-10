package ru.customer.petproject.customerservice.unittests.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;

class FieldValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeEach
    void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void close() {
        validatorFactory.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"random@random.ru", "random@dot.com"})
    void validEmailTest(String email) {
        var emailDto = new EmailDto(email);
        var violations = validator.validate(emailDto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"random.com", "random.com", "random.com"})
    void invalidEmailTest(String email) {
        var emailDto = new EmailDto(email);
        var violations = validator.validate(emailDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void validEmail_EmailIsNull() {
        EmailDto emailDto = new EmailDto(null);
        Set<ConstraintViolation<EmailDto>> violations = validator.validate(emailDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void ValidSecurityQuestionAndAnswer_withRandomStringWithSpaces() {
        String validQuestion = "Super random string";
        String validAnswer = "Super random string";
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto =
            new SecurityQuestionAndAnswerDto(validQuestion, validAnswer);
        Set<ConstraintViolation<SecurityQuestionAndAnswerDto>> violations =
            validator.validate(securityQuestionAndAnswerDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void ValidSecurityQuestionAndAnswer_withSpecialSymbols() {
        String validQuestion = "String with ! ? , . _ - 1234567890";
        String validAnswer = "String with ! ? , . _ - 1234567890";
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto =
            new SecurityQuestionAndAnswerDto(validQuestion, validAnswer);
        Set<ConstraintViolation<SecurityQuestionAndAnswerDto>> violations =
            validator.validate(securityQuestionAndAnswerDto);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
        "qw, qw",
        "Random super long string with more than fifty symbols, Random super long string with more than fifty symbols",
        "Random string with :, Random string with :"
    })
    void validSecurityQuestionAndAnswer(String question, String answer) {
        int numberOfUnValidFields = 2;
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto =
            new SecurityQuestionAndAnswerDto(question, answer);
        Set<ConstraintViolation<SecurityQuestionAndAnswerDto>> violations =
            validator.validate(securityQuestionAndAnswerDto);
        assertEquals(numberOfUnValidFields, violations.size());
    }
}
