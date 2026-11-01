package incubyte.salmgmt.controllers;

import incubyte.salmgmt.dto.respose.AnalyticsOverviewResponseDto;
import incubyte.salmgmt.dto.respose.CountryPayrollResponseDto;
import incubyte.salmgmt.dto.respose.DepartmentPayrollResponseDto;
import incubyte.salmgmt.dto.respose.SalaryDistributionResponseDto;
import incubyte.salmgmt.repositories.SalaryStatisticsProjection;
import incubyte.salmgmt.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsOverviewResponseDto> getOverview() {
        return ResponseEntity.ok(analyticsService.getOverview());
    }

    @GetMapping("/payroll-by-country")
    public ResponseEntity<List<CountryPayrollResponseDto>> getPayrollByCountry() {
        return ResponseEntity.ok(analyticsService.getPayrollByCountry());
    }

    @GetMapping("/payroll-by-department")
    public ResponseEntity<List<DepartmentPayrollResponseDto>> getPayrollByDepartment() {
        return ResponseEntity.ok(analyticsService.getPayrollByDepartment());
    }

    @GetMapping("/salary-statistics")
    public ResponseEntity<SalaryStatisticsProjection> getSalaryStatistics() {
        return ResponseEntity.ok(analyticsService.getSalaryStatistics());
    }

    @GetMapping("/salary-distribution")
    public ResponseEntity<List<SalaryDistributionResponseDto>> getSalaryDistribution() {
        return ResponseEntity.ok(analyticsService.getSalaryDistribution());
    }
}