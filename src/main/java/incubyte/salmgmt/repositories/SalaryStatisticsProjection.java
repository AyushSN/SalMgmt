package incubyte.salmgmt.repositories;

import java.math.BigDecimal;

public interface SalaryStatisticsProjection {

    BigDecimal getMinimumSalaryUsd();

    BigDecimal getMaximumSalaryUsd();

    BigDecimal getAverageSalaryUsd();

    BigDecimal getMedianSalaryUsd();
}