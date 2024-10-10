package ru.zinovievbank.customerservice.repository;

import java.util.Optional;
import java.util.UUID;

import io.micrometer.core.annotation.Timed;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zinovievbank.customerservice.model.Customer;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT c.id FROM Customer c WHERE c.mobilePhone = ?1")
    Optional<UUID> findCustomerIdByMobilePhone(String mobilePhone);

    Optional<Customer> findCustomerById(UUID customerId);

    @Query("SELECT c.id FROM Customer c JOIN Passport p ON c.passport.id = p.id WHERE p.series = ?1 AND p.number = ?2")
    Optional<UUID> findCustomerIdByPassportNumber(String series, String passportNumber);
    
    @Query("SELECT c.customerStatus FROM Customer c WHERE c.mobilePhone = ?1")
    Optional<CustomerStatus> findCustomerStatusByMobilePhone(String mobilePhone);

    @Query("SELECT c.customerStatus FROM Customer c JOIN Passport p ON c.passport.id = p.id WHERE p.series = ?1 AND p.number = ?2")
    Optional<CustomerStatus> findCustomerStatusByPassportNumber(String series, String passportNumber);

    @EntityGraph(attributePaths = {"passport", "address"})
    @Timed("Full_Customer_Info_ById_Query")
    Optional<Customer> findFullCustomerInfoById(UUID customerId);

    Optional<Customer> findCustomerByMobilePhone (String phoneNumber);

    @Modifying
    @Query("UPDATE Customer c SET c.customerStatus = ?1 where c.id = ?2")
    void updateCustomerById(CustomerStatus customerStatus, UUID customerId);
}
