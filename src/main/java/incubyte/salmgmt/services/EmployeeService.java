package incubyte.salmgmt.services;

import incubyte.salmgmt.dto.request.EmployeeRequestDto;
import incubyte.salmgmt.dto.request.EmployeeStatusUpdateRequestDto;
import incubyte.salmgmt.dto.request.EmployeeUpdateRequestDto;
import incubyte.salmgmt.dto.request.SalaryUpdateRequestDto;
import incubyte.salmgmt.dto.respose.EmployeeResponseDto;
import incubyte.salmgmt.dto.respose.SalaryHistoryResponseDto;
import incubyte.salmgmt.entity.Employee;
import incubyte.salmgmt.entity.SalaryHistory;
import incubyte.salmgmt.enums.EmployeeStatus;
import incubyte.salmgmt.enums.Gender;
import incubyte.salmgmt.exceptions.ConflictException;
import incubyte.salmgmt.exceptions.ResourceNotFoundException;
import incubyte.salmgmt.repositories.EmployeeRepository;
import incubyte.salmgmt.repositories.SalaryHistoryRepository;
import incubyte.salmgmt.specifications.EmployeeSpecifications;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryHistoryRepository salaryHistoryRepository;

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getGender(),
                employee.getCountry(),
                employee.getDepartment(),
                employee.getJobTitle(),
                employee.getLevel(),
                employee.getHireDate(),
                employee.getStatus(),
                employee.getCurrency(),
                employee.getCurrentSalary()
        );
    }

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {
        if (employeeRepository.existsByEmail(request.email())) {
            throw new ConflictException(
                    "An employee with this email already exists"
            );
        }

        Employee employee = new Employee();

        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setGender(request.gender());
        employee.setCountry(request.country());
        employee.setDepartment(request.department());
        employee.setJobTitle(request.jobTitle());
        employee.setLevel(request.level());
        employee.setHireDate(request.hireDate());
        employee.setCurrency(request.currency());
        employee.setCurrentSalary(request.currentSalary());

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToResponseDto(savedEmployee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id){

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );
        return mapToResponseDto(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(
            String country,
            String department,
            String status,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            Pageable pageable
    ) {
        Specification<Employee> specification = Specification
                .where(EmployeeSpecifications.hasCountry(country))
                .and(EmployeeSpecifications.hasDepartment(department))
                .and(EmployeeSpecifications.hasStatus(status))
                .and(EmployeeSpecifications.hasMinimumSalary(minSalary))
                .and(EmployeeSpecifications.hasMaximumSalary(maxSalary));

        return employeeRepository.findAll(specification, pageable)
                .map(this::mapToResponseDto);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateRequestDto request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + id
                        )
                );

        if (employeeRepository.existsByEmailAndIdNot(request.email(), id)){
            throw new ConflictException(
                    "Another employee already uses this email"
            );
        }

        employee.setName(request.name());
        employee.setEmail(request.email());
        employee.setGender(Gender.valueOf(request.gender().toUpperCase()));
        employee.setCountry(request.country());
        employee.setDepartment(request.department());
        employee.setJobTitle(request.jobTitle());
        employee.setLevel(request.level());
        employee.setHireDate(request.hireDate());
        employee.setCurrency(request.currency());

        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponseDto(updatedEmployee);
    }

    @Transactional
    public EmployeeResponseDto updateEmployeeSalary(
            Long employeeId,
            SalaryUpdateRequestDto request
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + employeeId
                        )
                );

        BigDecimal oldSalary = employee.getCurrentSalary();

        if (oldSalary.compareTo(request.newSalary()) == 0) {
            throw new IllegalArgumentException(
                    "New salary must be different from the current salary"
            );
        }

        SalaryHistory salaryHistory = new SalaryHistory();
        salaryHistory.setEmployee(employee);
        salaryHistory.setOldAmount(oldSalary);
        salaryHistory.setNewAmount(request.newSalary());
        salaryHistory.setCurrency(employee.getCurrency());
        salaryHistory.setEffectiveDate(request.effectiveDate());
        salaryHistory.setReason(request.reason());
        salaryHistory.setCreatedAt(LocalDateTime.now());

        employee.setCurrentSalary(request.newSalary());

        salaryHistoryRepository.save(salaryHistory);
        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponseDto(updatedEmployee);
    }

    @Transactional(readOnly = true)
    public List<SalaryHistoryResponseDto> getEmployeeSalaryHistory(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + employeeId
                        )
                );

        return salaryHistoryRepository
                .findAllByEmployee_IdOrderByEffectiveDateDesc(employeeId)
                .stream()
                .map(history -> new SalaryHistoryResponseDto(
                        history.getId(),
                        history.getOldAmount(),
                        history.getNewAmount(),
                        history.getCurrency(),
                        history.getEffectiveDate(),
                        history.getReason(),
                        history.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public EmployeeResponseDto updateEmployeeStatus(
            Long employeeId,
            EmployeeStatusUpdateRequestDto request
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found with ID: " + employeeId
                        )
                );

        EmployeeStatus newStatus;

        try {
            newStatus = EmployeeStatus.valueOf(
                    request.status().trim().toUpperCase()
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid employee status. Allowed values: ACTIVE, TERMINATED"
            );
        }

        if (employee.getStatus() == newStatus) {
            throw new IllegalArgumentException(
                    "Employee already has status: " + newStatus
            );
        }

        employee.setStatus(newStatus);

        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponseDto(updatedEmployee);
    }
}
