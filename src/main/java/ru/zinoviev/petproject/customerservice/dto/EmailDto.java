package ru.zinovievbank.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ru.zinovievbank.customerservice.util.RegexUtil;

public record EmailDto(@NotNull @Email String email) {}
