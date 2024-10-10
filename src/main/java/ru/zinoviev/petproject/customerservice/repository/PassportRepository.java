package ru.zinovievbank.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zinovievbank.customerservice.model.Passport;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {

}