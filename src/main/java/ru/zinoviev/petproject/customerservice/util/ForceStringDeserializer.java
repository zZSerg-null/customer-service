package ru.zinovievbank.customerservice.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;

import java.io.IOException;

/**
 * Custom ForceStringDeserializer.
 * Avoids auto-conversion from int to string
 */
public class ForceStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext){
        if (!JsonToken.VALUE_STRING.equals(jsonParser.getCurrentToken())) {
            throw new AppException(EnumException.UNSUPPORTED_MEDIA_TYPE);
        }
        try {
            return jsonParser.getValueAsString();
        } catch (IOException e) {
            throw new AppException(EnumException.INTERNAL_SERVER_ERROR);
        }
    }
}