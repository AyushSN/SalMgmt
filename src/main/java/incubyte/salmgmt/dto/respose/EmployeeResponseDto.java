package incubyte.salmgmt.dto.respose;

import incubyte.salmgmt.enums.EmployeeStatus;
import incubyte.salmgmt.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponseDto(

        Long id,
        String name,
        String email,
        Gender gender,
        String country,
        String department,
        String jobTitle,
        String level,
        LocalDate hireDate,
        EmployeeStatus status,
        String currency,
        BigDecimal currentSalary

) {}