package ru.customer.petproject.customerservice.unittests.util.enums.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

class CustomerStatusConverterTest {

    private final CustomerStatusConverter testInstance = new CustomerStatusConverter();

    @Test
    void convertToDatabaseColumn_shouldConvertStringCode() {
        for (CustomerStatus status : CustomerStatus.values()) {
            String code = testInstance.convertToDatabaseColumn(status);
            assertEquals(status.getCode(), code);
        }
    }

    @Test
    void convertToDatabaseColumn_shouldReturnNull() {
        String result = testInstance.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute_shouldCovertToEnum() {
        for (CustomerStatus status : CustomerStatus.values()) {
            assertEquals(status, testInstance.convertToEntityAttribute(status.getCode()));
        }
    }

    @Test
    void shouldReturnNullWithNullArgument() {
        testInstance.convertToEntityAttribute(null);
        assertNull(testInstance.convertToEntityAttribute(null));
        assertNull(testInstance.convertToEntityAttribute(null));
    }
}
