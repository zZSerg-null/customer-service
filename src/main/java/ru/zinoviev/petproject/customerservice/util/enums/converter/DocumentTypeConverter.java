package ru.zinovievbank.customerservice.util.enums.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;
import ru.zinovievbank.customerservice.util.enums.DocumentType;

@Converter(autoApply = true)
public class DocumentTypeConverter implements AttributeConverter<DocumentType, String> {

    @Override
    public String convertToDatabaseColumn(DocumentType documentType) {
        if (documentType == null) {
            return null;
        }
        return documentType.getCode();
    }

    @Override
    public DocumentType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(DocumentType.values())
            .filter(c -> c.getCode().equals(code))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Wrong DocumentType value"));
    }
}