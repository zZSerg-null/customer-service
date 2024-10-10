package ru.zinovievbank.customerservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.zinovievbank.customerservice.util.enums.CustomerStatus;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private UUID id;

    @NotNull
    @Column(name = "first_name", length = 30)
    private String firstName;

    @NotNull
    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "patronymic", length = 30)
    private String patronymic;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Pattern(regexp = "\\d{11}")
    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "inn", length = 12)
    private String inn;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    private Passport passport;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @NotNull
    @Column(name = "accession_date")
    private LocalDate accessionDate;

    @NotNull
    @Column(name = "customer_status")
    private CustomerStatus customerStatus;

    @NotNull
    @Column(name = "security_question", length = 50)
    private String securityQuestion;

    @NotNull
    @Column(name = "security_answer", length = 50)
    private String securityAnswer;

    @NotNull
    @Column(name = "sms_notification")
    private Boolean smsNotification;

    @NotNull
    @Column(name = "push_notification")
    private Boolean pushNotification;

    @NotNull
    @Column(name = "email_subscription")
    private Boolean emailSubscription;
}