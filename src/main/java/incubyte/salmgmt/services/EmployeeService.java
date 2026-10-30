package incubyte.salmgmt.services;

import incubyte.salmgmt.dto.request.EmployeeRequestDto;
import incubyte.salmgmt.dto.respose.EmployeeResponseDto;
import incubyte.salmgmt.entity.Employee;
import incubyte.salmgmt.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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

        return new EmployeeResponseDto(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getGender(),
                savedEmployee.getCountry(),
                savedEmployee.getDepartment(),
                savedEmployee.getJobTitle(),
                savedEmployee.getLevel(),
                savedEmployee.getHireDate(),
                savedEmployee.getStatus(),
                savedEmployee.getCurrency(),
                savedEmployee.getCurrentSalary()
        );
    }
}
