package incubyte.salmgmt.controllers;

import incubyte.salmgmt.dto.request.EmployeeRequestDto;
import incubyte.salmgmt.dto.respose.EmployeeResponseDto;
import incubyte.salmgmt.services.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;

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

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,

            @PageableDefault(
                    size = 20,
                    sort = "id",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                employeeService.getAllEmployees(
                        country,
                        department,
                        status,
                        minSalary,
                        maxSalary,
                        pageable
                )
        );
    }
}
