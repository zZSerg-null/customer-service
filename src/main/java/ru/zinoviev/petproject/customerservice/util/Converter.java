package ru.zinovievbank.customerservice.util;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@Slf4j
@Component
public class Converter {

    private final ModelMapper modelMapper;

    public Converter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerInfoDto convertToCustomerInfoDto(Customer customer) {
        CustomerInfoDto customerInfoDto = modelMapper.map(customer, CustomerInfoDto.class);
        if (customerInfoDto.getCustomerStatus() != null) {
            customerInfoDto.setCustomerStatus(
                CustomerStatus.valueOf(customerInfoDto.getCustomerStatus()).getCode());
        } else {
            log.debug("Method convertToCustomerInfoDto: customer status is null - customerId {}",
                customer.getId());
        }
        return customerInfoDto;
    }

    public CustomerNotificationsDto convertToCustomerNotificationsDto(Customer customer) {
        return modelMapper.map(customer, CustomerNotificationsDto.class);
    }
}
