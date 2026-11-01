package incubyte.salmgmt.repositories;

import java.math.BigDecimal;

public interface CountryPayrollProjection {

    String getCountry();

    Long getTotalEmployees();

    BigDecimal getTotalPayrollUsd();
}
