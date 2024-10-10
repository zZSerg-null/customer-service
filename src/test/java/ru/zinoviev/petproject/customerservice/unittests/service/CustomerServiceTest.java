package ru.customer.petproject.customerservice.unittests.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;
import ru.zinovievbank.customerservice.dto.CustomerInfoDto;
import ru.zinovievbank.customerservice.dto.EmailDto;
import ru.zinovievbank.customerservice.dto.SecurityQuestionAndAnswerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaCustomerDto;
import ru.zinovievbank.customerservice.dto.kafka.KafkaUpdateCustomerDto;
import ru.zinovievbank.customerservice.mapper.KafkaCustomerMapper;
import ru.zinovievbank.customerservice.model.Address;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.model.Passport;
import ru.zinovievbank.customerservice.repository.AddressRepository;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.repository.PassportRepository;
import ru.zinovievbank.customerservice.service.CustomerGrpcService;
import ru.zinovievbank.customerservice.service.CustomerService;
import ru.zinovievbank.customerservice.util.CustomerMapper;
import ru.zinovievbank.customerservice.util.CustomerMapperImpl;
import ru.zinovievbank.customerservice.auth.JwtProvider;
import ru.zinovievbank.customerservice.util.MaskData;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;
import ru.zinovievbank.customerservice.util.enums.converter.CustomerStatusConverter;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.customer.petproject.customerservice.unittests.util.TestConstants.REFRESH_TOKEN;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private final String FIRST_NAME = "Alex";
    private final String LAST_NAME = "Grey";
    private final String PATRONYMIC = "Patronymic";
    private final String EMAIL = "test@test.te";
    private final String MOBILE_PHONE = "12345678901";
    private final String SECURITY_QUESTION = "Random question";
    private final String SECURITY_ANSWER = "Random answer";
    private final UUID CUSTOMER_ID = UUID.fromString("6733729f-d627-4122-baab-8100d7242f30");
    private Customer customer;
    private Address address;
    private Passport passport;
    private final KafkaUpdateCustomerDto kafkaUpdateCustomerDto = KafkaUpdateCustomerDto.builder()
            .customerUuid(CUSTOMER_ID)
            .build();
    private final KafkaCustomerDto kafkaCustomerDto = KafkaCustomerDto.builder()
            .customerUuid(CUSTOMER_ID)
            .build();

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerGrpcService customerGrpcService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PassportRepository passportRepository;

    @Mock
    private KafkaCustomerMapper kafkaCustomerMapper;

    @Spy
    private final CustomerMapper mapper = new CustomerMapperImpl(new CustomerStatusConverter());

    @SuppressWarnings("unused")
    @Spy
    private MaskData maskData;

    @Mock
    private JwtProvider jwtProvider;

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
        customer.setSecurityQuestion(SECURITY_QUESTION);
        customer.setSecurityAnswer(SECURITY_ANSWER);
        customer.setEmail(EMAIL);

        address = new Address();
        address.setId(1L);

        passport = new Passport();
        passport.setId(1L);
    }

    @Test
    @DisplayName("CRS-8: should return CustomerInfoDto if customer id is valid and customer exist")
    void getCustomerInfoById_ShouldReturnCustomerInfoDto_IfCustomerIdIsValidAndCustomerExist() throws JsonProcessingException {

        CustomerInfoDto expectedCustomerInfoDto = new CustomerInfoDto();
        expectedCustomerInfoDto.setFirstName(FIRST_NAME);
        expectedCustomerInfoDto.setLastName(LAST_NAME);
        expectedCustomerInfoDto.setPatronymic(PATRONYMIC);
        expectedCustomerInfoDto.setBirthDate(LocalDate.now());
        expectedCustomerInfoDto.setMobilePhone(MOBILE_PHONE);
        expectedCustomerInfoDto.setEmail(EMAIL);
        expectedCustomerInfoDto.setCustomerStatus(CustomerStatus.CUSTOMER_NOT_REGISTERED.getCode());

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        String customerDto = customerService.getCustomerInfoById(REFRESH_TOKEN);

        String request = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build()
                .writeValueAsString(expectedCustomerInfoDto);

        assertEquals(request, customerDto);
    }

    @Test
    @DisplayName("CRS-8: should throw an AppException if the customer id is invalid")
    void getCustomerInfoById_ShouldThrowAppException_IfCustomerIdIsNotValid() {
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        var customerId = customer.getId();
        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        var customerIdString = customerId.toString();
        AppException exception = assertThrows(AppException.class,
                () -> customerService.getCustomerInfoById(REFRESH_TOKEN));
        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-8: should throw an AppException if the customer id is invalid")
    void getCustomerInfoById_ShouldThrowAppException_IfCustomerIdIsNotFound() {
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        var customerId = CUSTOMER_ID.toString();
        AppException exception = assertThrows(AppException.class,
                () -> customerService.getCustomerInfoById(REFRESH_TOKEN));
        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-15: should update security question and security answer")
    void should_updateSecurityQuestion_SecurityAnswer_with_Empty_Email() {
        String newSecurityQuestion = "Random question";
        String newSecurityAnswer = "Random answer";
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto =
                new SecurityQuestionAndAnswerDto(newSecurityQuestion, newSecurityAnswer);

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.updateSecurityQuestionAndAnswer(REFRESH_TOKEN,
                securityQuestionAndAnswerDto);

        assertEquals(newSecurityQuestion, customer.getSecurityQuestion());
        assertEquals(newSecurityAnswer, customer.getSecurityAnswer());
        assertEquals(EMAIL, customer.getEmail());
    }

    @Test
    @DisplayName("CRS-15: should update security question, security answer")
    void should_updateSecurityQuestion_SecurityAnswer_with_Email() {
        String newSecurityQuestion = "Random question";
        String newSecurityAnswer = "Random answer";
        SecurityQuestionAndAnswerDto securityQuestionAndAnswerDto =
                new SecurityQuestionAndAnswerDto(newSecurityQuestion, newSecurityAnswer);

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.updateSecurityQuestionAndAnswer(REFRESH_TOKEN,
                securityQuestionAndAnswerDto);
        assertEquals(newSecurityQuestion, customer.getSecurityQuestion());
        assertEquals(newSecurityAnswer, customer.getSecurityAnswer());
    }

    @Test
    @DisplayName("CRS-9: update email should update email and return 200 OK")
    void updateEmail_ShouldUpdateEmailAndReturn200OK() {
        String newEmail = "newEmail@example.com";
        EmailDto emailDto = new EmailDto(newEmail);
        customer.setCustomerStatus(CustomerStatus.CUSTOMER_REGISTERED);

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.updateEmail(REFRESH_TOKEN, emailDto);
        assertEquals(newEmail, customer.getEmail());
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    @DisplayName("CRS-9: update email should throw AppException if customer id is not valid")
    void updateEmail_ShouldThrowAppException_IfCustomerIdIsNotValid() {
        var newEmail = "newEmail@example.com";
        var emailDto = new EmailDto(newEmail);
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> customerService.updateEmail(REFRESH_TOKEN, emailDto));
        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }

    @Test
    @DisplayName("CRS-9: update email should throw AppException if customer id is not found")
    void updateEmail_ShouldThrowAppException_IfCustomerIdIsNotFound() {
        String newEmail = "newEmail@example.com";
        EmailDto emailDto = new EmailDto(newEmail);
        EnumException expectedEnumException = EnumException.BAD_REQUEST;

        when(jwtProvider.extractCustomerId(anyString()))
                .thenReturn(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class,
                () -> customerService.updateEmail(REFRESH_TOKEN, emailDto));
        assertEquals(expectedEnumException, exception.getEnumExceptions());
    }

    @Test
    void addNewCustomer_ShouldAddNewCustomer() {
        when(customerRepository.existsById(kafkaCustomerDto.customerUuid())).thenReturn(false);
        when(kafkaCustomerMapper.toCustomer(kafkaCustomerDto)).thenReturn(customer);
        when(kafkaCustomerMapper.toAddress(kafkaCustomerDto)).thenReturn(address);
        when(kafkaCustomerMapper.toPassport(kafkaCustomerDto)).thenReturn(passport);

        customerService.addNewCustomer(kafkaCustomerDto);

        verify(customerRepository, times(1)).save(customer);
        verify(addressRepository, times(1)).save(address);
        verify(passportRepository, times(1)).save(passport);
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer() {
        when(customerRepository.findFullCustomerInfoById(kafkaUpdateCustomerDto.customerUuid()))
                .thenReturn(Optional.of(customer));

        customerService.updateCustomer(kafkaUpdateCustomerDto);

        verify(kafkaCustomerMapper).updateCustomer(eq(kafkaUpdateCustomerDto), same(customer));
        verify(kafkaCustomerMapper).updateAddress(eq(kafkaUpdateCustomerDto),
                same(customer.getAddress()));
        verify(kafkaCustomerMapper).updatePassport(eq(kafkaUpdateCustomerDto),
                same(customer.getPassport()));
        verify(customerRepository, times(1)).save(same(customer));
    }

    @Test
    void addNewCustomer_ShouldNotDoAnything_WhenCustomerNotExist() {
        when(customerRepository.existsById(kafkaCustomerDto.customerUuid()))
                .thenReturn(true);

        customerService.addNewCustomer(kafkaCustomerDto);

        verify(customerRepository, never()).save(customer);
        verify(addressRepository, never()).save(address);
        verify(passportRepository, never()).save(passport);
    }

    @Test
    void updateCustomer_ShouldNotDoAnything_WhenCustomerNotExist() {
        when(customerRepository.findFullCustomerInfoById(
                kafkaUpdateCustomerDto.customerUuid())).thenReturn(Optional.empty());

        customerService.updateCustomer(kafkaUpdateCustomerDto);

        verify(kafkaCustomerMapper, never()).updateCustomer(any(), any());
        verify(kafkaCustomerMapper, never()).updateAddress(any(), any());
        verify(kafkaCustomerMapper, never()).updatePassport(any(), any());
        verify(customerRepository, never()).save(any());
    }
    @Test
    @DisplayName("Пользователь найден в локальной БД со статусом CUSTOMER_BLOCKED")
    void checkCustomerByPhoneNumber_ShouldThrowAppException_IfCustomerBlockedInLocal() {
        Customer customer = mock(Customer.class);
        when(customerRepository.findCustomerByMobilePhone(anyString())).thenReturn(Optional.of(customer));

        when(customer.getCustomerStatus()).thenReturn(CustomerStatus.CUSTOMER_BLOCKED);

        AppException thrown = assertThrows(AppException.class,
                () -> customerService.checkCustomerByPhoneNumber(anyString()));

        assertEquals(EnumException.FORBIDDEN, thrown.getEnumExceptions());

        verify(customerRepository).findCustomerByMobilePhone(anyString());

    }

    @Test
    @DisplayName("Пользователь найден в локальной БД со статусом CUSTOMER_NOT_REGISTERED")
    void checkCustomerByPhoneNumber_ShouldThrowAppException_IfCustomerNotRegisteredInLocal() {
        Customer customer = mock(Customer.class);
        when(customerRepository.findCustomerByMobilePhone(anyString())).thenReturn(Optional.of(customer));

        when(customer.getCustomerStatus()).thenReturn(CustomerStatus.CUSTOMER_NOT_REGISTERED);

        AppException thrown = assertThrows(AppException.class,
                () -> customerService.checkCustomerByPhoneNumber(anyString()));
        assertEquals(EnumException.BAD_REQUEST, thrown.getEnumExceptions());

        verify(customerRepository).findCustomerByMobilePhone(anyString());
    }

    @Test
    @DisplayName("Пользователь найден в локальной БД со статусом CUSTOMER_REGISTERED")
    void checkCustomerByPhoneNumber_OK() {
        Customer customer = mock(Customer.class);
        when(customerRepository.findCustomerByMobilePhone(anyString())).thenReturn(Optional.of(customer));

        when(customer.getCustomerStatus()).thenReturn(CustomerStatus.CUSTOMER_REGISTERED);
        customerService.checkCustomerByPhoneNumber(anyString());

        verify(customerRepository).findCustomerByMobilePhone(anyString());
    }

    @Test
    @DisplayName("Пользователь не найден ни в локальной БД, ни в ABS")
    void checkCustomerByPhoneNumber_ShouldThrowAppException_IfCustomerNotFoundNowhere() {
        when(customerRepository.findCustomerByMobilePhone(anyString())).thenReturn(Optional.empty());

        when(customerGrpcService.getCustomerByMobilePhone(anyString())).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class,
                () -> customerService.checkCustomerByPhoneNumber(anyString()));
        assertEquals(EnumException.NOT_FOUND, thrown.getEnumExceptions());

        verify(customerRepository).findCustomerByMobilePhone(anyString());

    }

    @Test
    @DisplayName("Пользователь не найден в локальной БД, но найден в ABS")
    void checkCustomerByPhoneNumber_shouldThrowAppException_IfCustomerFoundInABS() {
        Customer customer = mock(Customer.class);
        when(customerRepository.findCustomerByMobilePhone(anyString())).thenReturn(Optional.empty());

        when(customerGrpcService.getCustomerByMobilePhone(anyString())).thenReturn(Optional.of(customer));

        AppException thrown = assertThrows(AppException.class,
                () -> customerService.checkCustomerByPhoneNumber(anyString()));

        assertEquals(EnumException.BAD_REQUEST, thrown.getEnumExceptions());

        verify(customerRepository).findCustomerByMobilePhone(anyString());

    }

}
