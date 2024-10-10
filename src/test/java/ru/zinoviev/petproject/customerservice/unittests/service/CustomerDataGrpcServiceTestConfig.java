package ru.customer.petproject.customerservice.unittests.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.zinovievbank.customerservice.repository.CustomerRepository;
import ru.zinovievbank.customerservice.service.CustomerDataGrpcService;

@TestConfiguration
public class CustomerDataGrpcServiceTestConfig {

    @Bean
    public CustomerDataGrpcService service(CustomerRepository customerRepository) {
        return new CustomerDataGrpcService(customerRepository);
    }
}