package ru.customer.petproject.customerservice.unittests.util.enums.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.zinovievbank.customerservice.util.enums.DocumentType;
import ru.zinovievbank.customerservice.util.enums.converter.DocumentTypeConverter;

class DocumentTypeConverterTest {

    private final DocumentTypeConverter testInstance = new DocumentTypeConverter();

    @Test
    @DisplayName("Convert to database column should convert string code")
    void convertToDatabaseColumn_shouldConvertStringCode() {
        for (DocumentType type : DocumentType.values()) {
            String code = testInstance.convertToDatabaseColumn(type);
            assertEquals(type.getCode(), code);
        }
    }

    @Test
    @DisplayName("Convert to database column should return null")
    void convertToDatabase_shouldReturnNull() {
        String result = testInstance.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    @DisplayName("Convert to entity attribute should convert to enum")
    void convertToEntityAttribute_shouldConvertToEnum() {
        for (DocumentType type : DocumentType.values()) {
            assertEquals(type, testInstance.convertToEntityAttribute(type.getCode()));
        }
    }

    @Test
    @DisplayName("Convert to entity attribute should return null with null argument")
    void shouldReturnNullWithNullArgument() {
        testInstance.convertToEntityAttribute(null);
        assertNull(testInstance.convertToEntityAttribute(null));
        assertNull(testInstance.convertToEntityAttribute(null));
    }
}
