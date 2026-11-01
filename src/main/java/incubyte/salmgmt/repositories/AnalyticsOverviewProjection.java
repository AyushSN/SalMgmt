package incubyte.salmgmt.repositories;

import java.math.BigDecimal;

public interface AnalyticsOverviewProjection {
    Long getTotalEmployees();

    BigDecimal getTotalPayrollUsd();

    BigDecimal getAverageSalaryUsd();
}
