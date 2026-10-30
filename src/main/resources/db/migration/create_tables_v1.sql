-- EMPLOYEE TABLE


CREATE TABLE employee (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                          name varchar(150) not null,
                          email varchar(255) not null unique,

                          gender varchar(20),

                          country varchar(100) not null,
                          department varchar(100) not null,
                          job_title varchar(150) not null,
                          level varchar(50) not null,

                          hire_date date not null,

                          status varchar(20) not null default 'ACTIVE',

                          currency CHAR(3) not null,
                          current_salary numeric(19, 2) not null,

                          created_at timestamp not null default CURRENT_TIMESTAMP,
                          updated_at timestamp not null default CURRENT_TIMESTAMP,

                          constraint chk_employee_status check (status IN ('ACTIVE', 'TERMINATED')),

                          constraint chk_employee_salary check (current_salary >= 0)
);


-- SALARY HISTORY TABLE


CREATE TABLE salary_history (
                                id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                                employee_id BIGINT not null,

                                old_amount numeric(19, 2) not null,
                                new_amount numeric(19, 2) not null,

                                currency char(3) not null,

                                effective_date date not null,

                                reason varchar(500) not null,

                                created_at timestamp not null default CURRENT_TIMESTAMP,

                                constraint fk_salary_history_employee
                                    foreign key (employee_id)
                                        references employee(id),

                                constraint chk_salary_history_amounts
                                    check (old_amount >= 0 AND new_amount >= 0)
);


-- FX RATE TABLE

CREATE TABLE fx_rate (
                         currency char(3) primary key,

                         rate_to_usd numeric(19, 8) not null,

                         constraint chk_fx_rate_positive
                             check (rate_to_usd > 0)
);


-- INDEXES

CREATE INDEX idx_employee_country
    ON employee(country);

CREATE INDEX idx_employee_department
    ON employee(department);

CREATE INDEX idx_employee_status
    ON employee(status);

CREATE INDEX idx_employee_level
    ON employee(level);

CREATE INDEX idx_salary_history_employee_date
    ON salary_history(employee_id, effective_date DESC);