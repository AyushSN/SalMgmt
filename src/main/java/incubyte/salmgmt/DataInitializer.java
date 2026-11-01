package incubyte.salmgmt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private static final int TARGET_EMPLOYEES = 10_000;
    private static final int BATCH_SIZE = 500;

    private static final Random RANDOM = new Random(42);

    private static final String[] COUNTRIES = {
            "USA", "India", "UK", "Germany",
            "Canada", "Australia", "Singapore"
    };

    private static final String[] DEPARTMENTS = {
            "Engineering", "Human Resources", "Finance",
            "Marketing", "Sales", "Operations", "Product"
    };

    private static final String[] LEVELS = {
            "L1", "L2", "L3", "L4", "L5"
    };

    private static final String[] GENDERS = {
            "MALE", "FEMALE", "OTHER"
    };

    private static final String[] FIRST_NAMES = {
            "John", "Jane", "Michael", "Sarah", "David",
            "Emily", "Robert", "Olivia", "Daniel", "Sophia",
            "James", "Emma", "William", "Ava", "Alexander",
            "Isabella", "Thomas", "Mia", "Benjamin", "Charlotte"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Garcia", "Miller", "Davis", "Wilson", "Anderson",
            "Taylor", "Thomas", "Moore", "Martin", "Jackson",
            "Thompson", "White", "Harris", "Clark", "Lewis"
    };

    private static final Map<String, String> COUNTRY_CURRENCY = Map.of(
            "USA", "USD",
            "India", "INR",
            "UK", "GBP",
            "Germany", "EUR",
            "Canada", "CAD",
            "Australia", "AUD",
            "Singapore", "SGD"
    );

    private static final Map<String, String[]> DEPARTMENT_JOB_TITLES = Map.of(
            "Engineering", new String[]{
                    "Software Engineer",
                    "Senior Software Engineer",
                    "Engineering Manager"
            },
            "Human Resources", new String[]{
                    "HR Specialist",
                    "HR Manager",
                    "HR Business Partner"
            },
            "Finance", new String[]{
                    "Financial Analyst",
                    "Accountant",
                    "Finance Manager"
            },
            "Marketing", new String[]{
                    "Marketing Executive",
                    "Marketing Manager"
            },
            "Sales", new String[]{
                    "Sales Executive",
                    "Sales Manager"
            },
            "Operations", new String[]{
                    "Operations Analyst",
                    "Operations Manager"
            },
            "Product", new String[]{
                    "Product Analyst",
                    "Product Manager"
            }
    );

    private static final Map<String, BigDecimal[]> LEVEL_SALARY_RANGES = Map.of(
            "L1", new BigDecimal[]{
                    BigDecimal.valueOf(25_000),
                    BigDecimal.valueOf(45_000)
            },
            "L2", new BigDecimal[]{
                    BigDecimal.valueOf(40_000),
                    BigDecimal.valueOf(65_000)
            },
            "L3", new BigDecimal[]{
                    BigDecimal.valueOf(60_000),
                    BigDecimal.valueOf(90_000)
            },
            "L4", new BigDecimal[]{
                    BigDecimal.valueOf(85_000),
                    BigDecimal.valueOf(120_000)
            },
            "L5", new BigDecimal[]{
                    BigDecimal.valueOf(110_000),
                    BigDecimal.valueOf(160_000)
            }
    );

    @Override
    public void run(String... args) {

        seedFxRates();

        Integer existingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM employee",
                Integer.class
        );

        int currentCount = existingCount == null ? 0 : existingCount;

        if (currentCount >= TARGET_EMPLOYEES) {
            System.out.println("Employee seed data already exists. Skipping initialization.");
            return;
        }

        int employeesToCreate = TARGET_EMPLOYEES - currentCount;

        System.out.println("Existing employees: " + currentCount);
        System.out.println("Employees to generate: " + employeesToCreate);

        Map<String, BigDecimal> fxRates = loadFxRates();

        generateEmployees(currentCount, employeesToCreate, fxRates);

        System.out.println("Employee seeding completed successfully.");

        Integer finalCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM employee",
                Integer.class
        );

        System.out.println("Total employees in database: " + finalCount);
    }

    private void seedFxRates() {

        String sql = """
                INSERT INTO fx_rate (currency, rate_to_usd)
                VALUES
                    ('USD', 1.00000000),
                    ('INR', 0.01200000),
                    ('GBP', 1.27000000),
                    ('EUR', 1.08000000),
                    ('CAD', 0.73000000),
                    ('AUD', 0.66000000),
                    ('SGD', 0.74000000)
                ON CONFLICT (currency) DO NOTHING
                """;

        jdbcTemplate.update(sql);
    }

    private Map<String, BigDecimal> loadFxRates() {

        return jdbcTemplate.query(
                "SELECT currency, rate_to_usd FROM fx_rate",
                rs -> {
                    Map<String, BigDecimal> rates = new HashMap<>();

                    while (rs.next()) {
                        rates.put(
                                rs.getString("currency"),
                                rs.getBigDecimal("rate_to_usd")
                        );
                    }

                    return rates;
                }
        );
    }

    private void generateEmployees(
            int existingCount,
            int employeesToCreate,
            Map<String, BigDecimal> fxRates
    ) {

        String sql = """
                INSERT INTO employee (
                    name,
                    email,
                    gender,
                    country,
                    department,
                    job_title,
                    level,
                    hire_date,
                    status,
                    currency,
                    current_salary
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        List<Object[]> batch = new ArrayList<>(BATCH_SIZE);

        for (int i = 1; i <= employeesToCreate; i++) {

            int employeeNumber = existingCount + i;

            String firstName = randomElement(FIRST_NAMES);
            String lastName = randomElement(LAST_NAMES);

            String name = firstName + " " + lastName;

            String email = String.format(
                    "seed.employee.%05d@example.test",
                    employeeNumber
            );

            String country = randomElement(COUNTRIES);
            String currency = COUNTRY_CURRENCY.get(country);

            String department = randomElement(DEPARTMENTS);

            String jobTitle = randomElement(
                    DEPARTMENT_JOB_TITLES.get(department)
            );

            String level = randomElement(LEVELS);

            String gender = randomElement(GENDERS);

            String status = RANDOM.nextDouble() < 0.90
                    ? "ACTIVE"
                    : "TERMINATED";

            LocalDate hireDate = generateHireDate();

            BigDecimal salary = generateSalary(level, currency, fxRates);

            batch.add(new Object[]{
                    name,
                    email,
                    gender,
                    country,
                    department,
                    jobTitle,
                    level,
                    hireDate,
                    status,
                    currency,
                    salary
            });

            if (batch.size() == BATCH_SIZE) {
                jdbcTemplate.batchUpdate(sql, batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batch);
        }
    }

    private BigDecimal generateSalary(
            String level,
            String currency,
            Map<String, BigDecimal> fxRates
    ) {

        BigDecimal[] range = LEVEL_SALARY_RANGES.get(level);

        BigDecimal minSalaryUsd = range[0];
        BigDecimal maxSalaryUsd = range[1];

        double randomValue = RANDOM.nextDouble();

        BigDecimal salaryUsd = minSalaryUsd.add(
                maxSalaryUsd.subtract(minSalaryUsd)
                        .multiply(BigDecimal.valueOf(randomValue))
        );

        BigDecimal rateToUsd = fxRates.get(currency);

        if (rateToUsd == null) {
            throw new IllegalStateException(
                    "Missing FX rate for currency: " + currency
            );
        }

        return salaryUsd.divide(
                rateToUsd,
                2,
                RoundingMode.HALF_UP
        );
    }

    private LocalDate generateHireDate() {

        LocalDate startDate = LocalDate.of(2015, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 10, 31);

        long daysBetween = endDate.toEpochDay() - startDate.toEpochDay();

        return startDate.plusDays(RANDOM.nextInt((int) daysBetween + 1));
    }

    private String randomElement(String[] values) {
        return values[RANDOM.nextInt(values.length)];
    }
}