package incubyte.salmgmt.repositories;

import java.math.BigDecimal;

public interface DepartmentPayrollProjection {

    String getDepartment();

    Long getTotalEmployees();

    BigDecimal getTotalPayrollUsd();
}
