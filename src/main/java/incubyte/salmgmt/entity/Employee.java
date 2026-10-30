package incubyte.salmgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

import incubyte.salmgmt.enums.EmployeeStatus;
import incubyte.salmgmt.enums.Gender;

@Entity
@Table(name="employee")
@Getter
@Setter
public class Employee{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(nullable = false, length = 50)
    private String level;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "current_salary", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentSalary;

}
