package incubyte.salmgmt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeUpdateRequestDto(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "Country is required")
        String country,

        @NotBlank(message = "Department is required")
        String department,

        @NotBlank(message = "Job title is required")
        String jobTitle,

        @NotBlank(message = "Level is required")
        String level,

        @NotNull(message = "Hire date is required")
        LocalDate hireDate,

        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-character code")
        String currency

) {}