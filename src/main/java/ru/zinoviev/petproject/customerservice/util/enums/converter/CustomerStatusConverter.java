package ru.zinovievbank.customerservice.util.enums.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@Converter(autoApply = true)
public class CustomerStatusConverter implements AttributeConverter<CustomerStatus, String> {

    @Override
    public String convertToDatabaseColumn(CustomerStatus status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public CustomerStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(CustomerStatus.values())
            .filter(c -> c.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Wrong CustomerStatus value"));
    }
}