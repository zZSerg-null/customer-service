package ru.customer.petproject.customerservice.unittests.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.zinovievbank.customerservice.util.MaskData;
import ru.zinovievbank.customerservice.util.enums.DataTypeToMask;

class MaskDataTest {

    private MaskData testInstance;

    @Test
    @DisplayName("Mask string should return masked email when email provided")
    void maskString_ShouldReturnMaskedEmail_WhenEmailProvided() {
        testInstance = new MaskData();
        String randomEmail0 = "asdfg@asd.ru";
        String randomEmail1 = "asdfghjk@asd.ru";
        String randomEmail2 = "asd_asd@asd.ru";

        String maskedRandomEmail0 = "as***@asd.ru";
        String maskedRandomEmail1 = "as******@asd.ru";
        String maskedRandomEmail2 = "as*****@asd.ru";

        assertEquals(maskedRandomEmail0, testInstance.mask(randomEmail0, DataTypeToMask.EMAIL));
        assertEquals(maskedRandomEmail1, testInstance.mask(randomEmail1, DataTypeToMask.EMAIL));
        assertEquals(maskedRandomEmail2, testInstance.mask(randomEmail2, DataTypeToMask.EMAIL));
    }

    @Test
    @DisplayName("Mask string should return masked mobile phone when mobile phone provided")
    void maskString_ShouldReturnMaskedMobilePhone_WhenMobilePhoneProvided() {
        testInstance = new MaskData();

        String randomPhoneNumber = "89110001234";
        String maskedRandomPhoneNumber = "*******1234";

        assertEquals(maskedRandomPhoneNumber,
            testInstance.mask(randomPhoneNumber, DataTypeToMask.MOBILE_PHONE));
    }

    @Test
    @DisplayName("Mask local date should return masked local date when local date provided")
    void maskLocalDate_ShouldReturnMaskedLocalDate_WhenLocalDateProvided() {
        testInstance = new MaskData();
        int year = 1999;
        int month = 1;
        int day = 10;
        LocalDate randomDate = LocalDate.of(year, month, day);
        String maskedStringDate = "1999-**-**";

        assertEquals(maskedStringDate, testInstance.maskLocalDate(randomDate));
    }

    @Test
    @DisplayName("Mask string should return masked last name when last name provided")
    void maskString_ShouldReturnMaskedLastName_WhenLastNameProvided() {
        testInstance = new MaskData();

        String randomLastName = "Ivanova";
        String maskedRandomLastName = "I******";
        String shortRandomLastName = "Ad";
        String maskedShortRandomLastName = "A*";

        assertEquals(maskedRandomLastName,
            testInstance.mask(randomLastName, DataTypeToMask.LAST_NAME));
        assertEquals(
            maskedShortRandomLastName,
            testInstance.mask(shortRandomLastName, DataTypeToMask.LAST_NAME));
    }

    @Test
    @DisplayName("Mask string should return masked series and passport number when series and passport number provided")
    void maskString_ShouldReturnMaskedSeriesAndPassportNumber_WhenSeriesAndPassportNumberProvided() {
        testInstance = new MaskData();

        String randomPassportNumber = "460690";
        String maskedDocNumber = "4****0";
        String randomSeries = "1111";
        String maskedRandomSeries = "1**1";

        assertEquals(maskedDocNumber,
            testInstance.mask(randomPassportNumber, DataTypeToMask.PASSPORT_NUMBER));
        assertEquals(maskedRandomSeries,
            testInstance.mask(randomSeries, DataTypeToMask.PASSPORT_NUMBER));
    }

    @Test
    @DisplayName("Test mask string should return masked string")
    void testMaskString_ShouldReturnMaskedString() {
        testInstance = new MaskData();

        String randomString = "test_dummy_string";
        String maskedString = "t****************";

        assertEquals(maskedString, testInstance.maskString(randomString));
    }

    @Test
    @DisplayName("Test mask string should return masked string with quantity of symbols")
    void testMaskString_ShouldReturnMaskedStringWithQuantityOfSymbols() {
        testInstance = new MaskData();

        String randomString = "test_dummy_string";

        int head0 = 0;
        int tail0 = 0;
        String maskedString0 = "*****************";
        int head1 = 1;
        int tail1 = 1;
        String maskedString1 = "t***************g";
        int head2 = 3;
        int tail2 = 3;
        String maskedString2 = "tes***********ing";
        int head3 = 2;
        int tail3 = 4;
        String maskedString3 = "te***********ring";
        int head4 = 2;
        int tail4 = 4;
        String maskedString4 = "te***********ring";
        int head5 = 6;
        int tail5 = 12;
        String maskedString5 = "t****************";
        int head6 = 6;
        int tail6 = 8;
        String maskedString6 = "t****************";
        assertEquals(maskedString0, testInstance.maskString(randomString, head0, tail0));
        assertEquals(maskedString1, testInstance.maskString(randomString, head1, tail1));
        assertEquals(maskedString2, testInstance.maskString(randomString, head2, tail2));
        assertEquals(maskedString3, testInstance.maskString(randomString, head3, tail3));
        assertEquals(maskedString4, testInstance.maskString(randomString, head4, tail4));
        assertEquals(maskedString5, testInstance.maskString(randomString, head5, tail5));
        assertEquals(maskedString6, testInstance.maskString(randomString, head6, tail6));
    }
}
