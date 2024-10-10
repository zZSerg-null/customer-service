package ru.zinovievbank.customerservice.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerStatus {
    CUSTOMER_NOT_REGISTERED("1"),
    CUSTOMER_REGISTERED("2"),
    CUSTOMER_BLOCKED("0");

    private final String code;

}