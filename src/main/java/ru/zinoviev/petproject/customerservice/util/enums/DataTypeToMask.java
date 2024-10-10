package ru.zinovievbank.customerservice.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataTypeToMask {
    EMAIL("email"),
    MOBILE_PHONE("mobilePhone"),
    SERIES("series"),
    PASSPORT_NUMBER("passportNumber"),
    LAST_NAME("lastName"), ;

    private final String value;
}
