package ru.zinovievbank.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zinovievbank.customerservice.model.Customer;

/**
 * DTO for {@link Customer}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfoDto implements Serializable {

    String firstName;
    String lastName;
    String patronymic;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;
    String mobilePhone;
    String email;
    String customerStatus;
}