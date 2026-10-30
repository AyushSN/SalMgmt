package incubyte.salmgmt.controllers;

import incubyte.salmgmt.dto.request.EmployeeRequestDto;
import incubyte.salmgmt.dto.respose.EmployeeResponseDto;
import incubyte.salmgmt.entity.Employee;
import incubyte.salmgmt.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employee) {
        EmployeeResponseDto response = employeeService.createEmployee(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
