package ru.zinovievbank.customerservice.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentType {
    PASSPORT_RF("1"),
    FOREIGN_PASSPORT("2"),
    RESIDENCE_PERMIT("3"),
    REFUGEE_CERTIFICATE("4"),
    OTHER("0");

    private final String code;

}