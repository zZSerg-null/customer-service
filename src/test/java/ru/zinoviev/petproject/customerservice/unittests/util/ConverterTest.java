package ru.customer.petproject.customerservice.unittests.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.CustomerNotificationsDto;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.util.Converter;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@ExtendWith(MockitoExtension.class)
class ConverterTest {

    private static final String FIRST_NAME = "Alex";
    private static final String LAST_NAME = "Grey";
    private static final String PATRONYMIC = "Patronymic";
    private static final String EMAIL = "test@test.te";
    private static final String MOBILE_PHONE = "12345678901";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private Customer customer;

    @InjectMocks
    private Converter converter;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setMobilePhone(MOBILE_PHONE);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        customer.setPatronymic(PATRONYMIC);
        customer.setBirthDate(LocalDate.now());
        customer.setCustomerStatus(CustomerStatus.CUSTOMER_NOT_REGISTERED);
        customer.setEmail(EMAIL);
    }

    @Test
    @DisplayName("Should convert customer to customerInfoDto")
    void should_return_customerInfoDto() {
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();

        when(modelMapper.map(customer, CustomerInfoDto.class)).thenReturn(customerInfoDto);
        CustomerInfoDto dto = converter.convertToCustomerInfoDto(customer);
        assertEquals(customerInfoDto, dto);
    }

    @Test
    @DisplayName("Should convert customer to customerNotificationsDto")
    void should_return_customerNotificationsDto() {
        CustomerNotificationsDto customerNotificationsDto = new CustomerNotificationsDto();

        when(modelMapper.map(customer, CustomerNotificationsDto.class)).thenReturn(
            customerNotificationsDto);
        CustomerNotificationsDto dto = converter.convertToCustomerNotificationsDto(customer);
        assertEquals(customerNotificationsDto, dto);
    }
}
