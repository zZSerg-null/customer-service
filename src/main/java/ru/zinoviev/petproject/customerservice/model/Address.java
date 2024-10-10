package ru.zinovievbank.customerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "country", length = 50)
    private String country;

    @NotNull
    @Column(name = "region", length = 50)
    private String region;

    @NotNull
    @Column(name = "city", length = 30)
    private String city;

    @NotNull
    @Column(name = "street", length = 30)
    private String street;

    @NotNull
    @Column(name = "house_number", length = 5)
    private String houseNumber;

    @Column(name = "entrance_number", length = 5)
    private String entranceNumber;

    @Column(name = "apartment_number")
    private Integer apartmentNumber;

    @NotNull
    @Column(name = "postcode")
    private Integer postcode;

    @NotNull
    @Column(name = "oktmo", length = 11)
    private String oktmo;

    @OneToOne(mappedBy = "address")
    private Customer customer;
}