package incubyte.salmgmt.dto.respose;

import java.math.BigDecimal;

public record CountryPayrollResponseDto(
        String country,
        Long totalEmployees,
        BigDecimal totalPayrollUsd
) {}
