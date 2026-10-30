package incubyte.salmgmt.services;

import incubyte.salmgmt.dto.request.EmployeeRequestDto;
import incubyte.salmgmt.dto.respose.EmployeeResponseDto;
import incubyte.salmgmt.entity.Employee;
import incubyte.salmgmt.repositories.EmployeeRepository;
import incubyte.salmgmt.specifications.EmployeeSpecifications;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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
        if(employeeRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("Employee already exists");
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
                    new IllegalArgumentException("Employee not found with id " + id)
                );
        return mapToResponseDto(employee);
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(String country,String department,String status,Pageable pageable) {

        Specification<Employee> specification = Specification
                .where(EmployeeSpecifications.hasCountry(country))
                .and(EmployeeSpecifications.hasDepartment(department))
                .and(EmployeeSpecifications.hasStatus(status));

        return employeeRepository.findAll(specification, pageable)
                .map(this::mapToResponseDto);
    }
}
