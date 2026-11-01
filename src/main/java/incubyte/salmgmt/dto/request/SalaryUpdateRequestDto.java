package incubyte.salmgmt.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryUpdateRequestDto (
    @NotNull(message = "New salary is required")
    @DecimalMin(value = "0.00", inclusive = false,
            message = "Salary must be greater than zero")
    BigDecimal newSalary,

    @NotNull(message = "Effective date is required")
    LocalDate effectiveDate,

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    String reason
){}
