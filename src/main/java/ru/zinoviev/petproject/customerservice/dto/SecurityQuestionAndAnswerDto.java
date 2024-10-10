package ru.zinovievbank.customerservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ru.zinovievbank.customerservice.util.RegexUtil;

public record SecurityQuestionAndAnswerDto(
    @NotNull
    @Pattern(
        regexp = RegexUtil.SECRET_QUESTION_AND_ANSWER,
        message = "Security Question is not correct, invalid length or characters")
    String securityQuestion,
    @NotNull
    @Pattern(
        regexp = RegexUtil.SECRET_QUESTION_AND_ANSWER,
        message = "Security answer is not correct, invalid length or characters")
    String securityAnswer) {

}
