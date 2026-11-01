package incubyte.salmgmt.repositories;

import incubyte.salmgmt.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Employee, Long> {
    @Query(value = """
            SELECT
                COUNT(e.id) AS total_employees,
                COALESCE(SUM(e.current_salary * fx.rate_to_usd), 0) AS total_payroll_usd,
                COALESCE(AVG(e.current_salary * fx.rate_to_usd), 0) AS average_salary_usd
            FROM employee e
            JOIN fx_rate fx ON e.currency = fx.currency
            WHERE e.status = 'ACTIVE'
            """, nativeQuery = true)
    AnalyticsOverviewProjection getOverview();

    @Query(value = """
    SELECT
        e.country AS "country",
        COUNT(e.id) AS "totalEmployees",
        COALESCE(SUM(e.current_salary * fx.rate_to_usd), 0) AS "totalPayrollUsd"
    FROM employee e
    JOIN fx_rate fx ON e.currency = fx.currency
    WHERE e.status = 'ACTIVE'
    GROUP BY e.country
    ORDER BY e.country
    """, nativeQuery = true)
    List<CountryPayrollProjection> getPayrollByCountry();

    @Query(value = """
    SELECT
        e.department AS "department",
        COUNT(e.id) AS "totalEmployees",
        COALESCE(SUM(e.current_salary * fx.rate_to_usd), 0) AS "totalPayrollUsd"
    FROM employee e
    JOIN fx_rate fx ON e.currency = fx.currency
    WHERE e.status = 'ACTIVE'
    GROUP BY e.department
    ORDER BY e.department
    """, nativeQuery = true)
    List<DepartmentPayrollProjection> getPayrollByDepartment();

    @Query(value = """
    SELECT
        COALESCE(MIN(e.current_salary * fx.rate_to_usd), 0) AS "minimumSalaryUsd",
        COALESCE(MAX(e.current_salary * fx.rate_to_usd), 0) AS "maximumSalaryUsd",
        COALESCE(AVG(e.current_salary * fx.rate_to_usd), 0) AS "averageSalaryUsd",
        COALESCE(
            PERCENTILE_CONT(0.5) WITHIN GROUP (
                ORDER BY e.current_salary * fx.rate_to_usd
            ), 0
        ) AS "medianSalaryUsd"
    FROM employee e
    JOIN fx_rate fx ON e.currency = fx.currency
    WHERE e.status = 'ACTIVE'
    """, nativeQuery = true)
    SalaryStatisticsProjection getSalaryStatistics();

    @Query(value = """
    SELECT
        CASE
            WHEN e.current_salary * fx.rate_to_usd < 30000 THEN 'Below 30K'
            WHEN e.current_salary * fx.rate_to_usd < 50000 THEN '30K - 49.9K'
            WHEN e.current_salary * fx.rate_to_usd < 75000 THEN '50K - 74.9K'
            WHEN e.current_salary * fx.rate_to_usd < 100000 THEN '75K - 99.9K'
            ELSE '100K+'
        END AS "salaryRange",
        COUNT(e.id) AS "employeeCount"
    FROM employee e
    JOIN fx_rate fx ON e.currency = fx.currency
    WHERE e.status = 'ACTIVE'
    GROUP BY
        CASE
            WHEN e.current_salary * fx.rate_to_usd < 30000 THEN 'Below 30K'
            WHEN e.current_salary * fx.rate_to_usd < 50000 THEN '30K - 49.9K'
            WHEN e.current_salary * fx.rate_to_usd < 75000 THEN '50K - 74.9K'
            WHEN e.current_salary * fx.rate_to_usd < 100000 THEN '75K - 99.9K'
            ELSE '100K+'
        END
    ORDER BY MIN(e.current_salary * fx.rate_to_usd)
    """, nativeQuery = true)
    List<SalaryDistributionProjection> getSalaryDistribution();
}
