package ru.zinovievbank.customerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "passport")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "citizenship")
    private String citizenship;

    @NotNull
    @Pattern(regexp = "^\\d{4}$")
    @Column(name = "series")
    private String series;

    @NotNull
    @Pattern(regexp = "^\\d{6}$")
    @Column(name = "number")
    private String number;

    @NotNull
    @Column(name = "issuance_date")
    private LocalDate issuanceDate;

    @NotNull
    @Column(name = "department_code")
    private String departmentCode;

    @NotNull
    @Column(name = "issued_by")
    private String issuedBy;

    @OneToOne(mappedBy = "passport")
    private Customer customer;
}