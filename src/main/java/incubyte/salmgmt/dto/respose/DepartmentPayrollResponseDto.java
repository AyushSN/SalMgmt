package incubyte.salmgmt.dto.respose;

import java.math.BigDecimal;

public record DepartmentPayrollResponseDto(
        String department,
        Long totalEmployees,
        BigDecimal totalPayrollUsd
) {}
