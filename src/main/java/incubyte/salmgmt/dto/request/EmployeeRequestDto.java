package incubyte.salmgmt.dto.request;

import incubyte.salmgmt.enums.Gender;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequestDto(

        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Email
        String email,

        Gender gender,

        @NotBlank
        String country,

        @NotBlank
        String department,

        @NotBlank
        String jobTitle,

        @NotBlank
        String level,

        @NotNull
        @PastOrPresent
        LocalDate hireDate,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter uppercase code")
        String currency,

        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 17, fraction = 2)
        BigDecimal currentSalary

) {}