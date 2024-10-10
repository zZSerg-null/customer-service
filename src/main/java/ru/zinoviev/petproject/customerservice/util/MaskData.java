package ru.zinovievbank.customerservice.util;

import java.time.LocalDate;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import ru.zinovievbank.customerservice.util.enums.DataTypeToMask;

@Component
public class MaskData {

    /**
     * Masks strings for specified data type. Mask examples:
     *
     * <p>email: s*****@example.com
     *
     * <p>mobile phone: *******0123
     *
     * <p>series: 1**1
     *
     * <p>passport number: 1****1
     *
     * <p>last name: A********
     *
     * @param stringToMask   - string to mask;
     * @param dataTypeToMask - {@link DataTypeToMask} for masking;
     * @return masked string.
     */
    public String mask(String stringToMask, DataTypeToMask dataTypeToMask) {

        int quantityHeadDigits = 0;
        int quantityTailDigits = 0;

        if (dataTypeToMask.equals(DataTypeToMask.EMAIL)) {
            int headIndex = 2;
            if (isValidEmailAddress(stringToMask)) {
                StringBuilder sb = new StringBuilder(stringToMask);
                for (int i = headIndex; i < sb.length() && sb.charAt(i) != '@'; ++i) {
                    sb.setCharAt(i, '*');
                }
                return sb.toString();
            } else {
                throw new IllegalArgumentException("The string to mask is not an email!");
            }
        }

        if (dataTypeToMask.equals(DataTypeToMask.MOBILE_PHONE)) {
            quantityTailDigits = 4;
            return maskString(stringToMask, quantityHeadDigits, quantityTailDigits);
        }

        if (dataTypeToMask.equals(DataTypeToMask.PASSPORT_NUMBER) || dataTypeToMask.equals(
            DataTypeToMask.SERIES)) {
            quantityHeadDigits = 1;
            quantityTailDigits = 1;
            String maskedString = maskString(stringToMask, quantityHeadDigits, quantityTailDigits);
            return maskString(maskedString, quantityHeadDigits, quantityTailDigits);
        }

        if (dataTypeToMask.equals(DataTypeToMask.LAST_NAME)) {
            quantityHeadDigits = 1;
            String maskedString = maskString(stringToMask, quantityHeadDigits, quantityTailDigits);
            return maskString(maskedString, quantityHeadDigits, quantityTailDigits);
        }

        return maskString(stringToMask, quantityHeadDigits, quantityTailDigits);
    }

    /**
     * Masks local date. Mask examples: LocalData: 2000-**-**
     *
     * @param dateToMask LocalDate to mask;
     * @return masked string.
     */
    public String maskLocalDate(LocalDate dateToMask) {
        return dateToMask.getYear() + "-**-**";
    }

    /**
     * Masks string, leaves only the first character visible.
     *
     * @param stringToMask - string to mask;
     * @return masked string.
     */
    public String maskString(String stringToMask) {
        int quantityHeadDigits = 1;
        int quantityTailDigits = 0;

        return maskString(stringToMask, quantityHeadDigits, quantityTailDigits);
    }

    /**
     * Masks string, leaves specified quantity of character visible.
     *
     * @param stringToMask      - string to mask;
     * @param qtyHeadCharacters - quantity of visible characters at the head of the string;
     * @param qtyTailCharacters - quantity of visible characters at the tail of the string;
     * @return masked string.
     */
    public String maskString(String stringToMask, int qtyHeadCharacters, int qtyTailCharacters) {

        if ((qtyHeadCharacters + qtyTailCharacters) > stringToMask.length() >> 1) {
            int head = 1;
            int tail = 0;
            return addAsterisk(head, stringToMask, tail);
        }

        return addAsterisk(qtyHeadCharacters, stringToMask, qtyTailCharacters);
    }

    private boolean isValidEmailAddress(String email) {
        return Pattern.compile(RegexUtil.EMAIL).matcher(email).matches();
    }

    private String addAsterisk(int qtyHeadCharacters, String stringToMask, int qtyTailCharacters) {
        StringBuilder sb = new StringBuilder(stringToMask);

        for (int i = qtyHeadCharacters; i < stringToMask.length() - qtyTailCharacters; i++) {
            sb.setCharAt(i, '*');
        }

        return sb.toString();
    }
}
