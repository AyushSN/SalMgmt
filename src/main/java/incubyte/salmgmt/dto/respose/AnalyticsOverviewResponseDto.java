package incubyte.salmgmt.dto.respose;

import java.math.BigDecimal;

public record AnalyticsOverviewResponseDto(
        Long totalEmployees,
        BigDecimal totalPayrollUsd,
        BigDecimal averageSalaryUsd
) {
}
