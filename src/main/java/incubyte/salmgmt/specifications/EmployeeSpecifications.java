package incubyte.salmgmt.specifications;

import incubyte.salmgmt.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class EmployeeSpecifications {

    public static Specification<Employee> hasCountry(String country) {
        return(root, query, cb) ->
                country == null || country.isBlank() ? null :
                        cb.equal(cb.lower(root.get("country")), country.trim().toLowerCase());
    }

    public static Specification<Employee> hasDepartment(String department) {
        return(root, query, cb) ->
                department == null || department.isBlank() ? null :
                        cb.equal(cb.lower(root.get("department")),
                                department.trim().toLowerCase()
                        );
    }

    public static Specification<Employee> hasStatus(String status) {
        return(root, query, cb) ->
                status == null || status.isBlank() ? null :
                        cb.equal(cb.lower(root.get("status")),
                                status.trim().toLowerCase()
                        );
    }

    public static Specification<Employee> hasMinimumSalary(BigDecimal minSalary) {
        return (root, query, cb) ->
                minSalary == null ? null :
                        cb.greaterThanOrEqualTo(
                                root.get("currentSalary"),
                                minSalary
                        );
    }

    public static Specification<Employee> hasMaximumSalary(BigDecimal maxSalary) {
        return (root, query, cb) ->
                maxSalary == null ? null :
                        cb.lessThanOrEqualTo(
                                root.get("currentSalary"),
                                maxSalary
                        );
    }
}
