package ru.zinovievbank.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zinovievbank.customerservice.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}