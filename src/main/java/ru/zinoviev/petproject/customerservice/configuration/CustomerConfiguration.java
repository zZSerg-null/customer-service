package ru.zinovievbank.customerservice.configuration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zinovievbank.customerservice.util.enums.NotificationType;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class CustomerConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CustomerStatusConverter customerStatusConverter() {
        return new CustomerStatusConverter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
            .findAndAddModules()
            .build();
    }

    @Bean
    public Map<NotificationType, Integer> partitionMap() {
        Map<NotificationType, Integer> partition = new EnumMap<>(NotificationType.class);
        partition.put(NotificationType.SMS, 0);
        partition.put(NotificationType.EMAIL, 1);
        partition.put(NotificationType.PUSH, 2);
        return partition;
    }
}
