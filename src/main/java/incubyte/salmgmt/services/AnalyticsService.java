package incubyte.salmgmt.services;

import incubyte.salmgmt.dto.respose.AnalyticsOverviewResponseDto;
import incubyte.salmgmt.dto.respose.CountryPayrollResponseDto;
import incubyte.salmgmt.dto.respose.DepartmentPayrollResponseDto;
import incubyte.salmgmt.dto.respose.SalaryDistributionResponseDto;
import incubyte.salmgmt.repositories.AnalyticsOverviewProjection;
import incubyte.salmgmt.repositories.AnalyticsRepository;
import incubyte.salmgmt.repositories.SalaryStatisticsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    @Transactional(readOnly = true)
    public AnalyticsOverviewResponseDto getOverview() {

        AnalyticsOverviewProjection result =
                analyticsRepository.getOverview();

        return new AnalyticsOverviewResponseDto(
                result.getTotalEmployees(),
                result.getTotalPayrollUsd(),
                result.getAverageSalaryUsd()
        );
    }

    @Transactional(readOnly = true)
    public List<CountryPayrollResponseDto> getPayrollByCountry() {
        return analyticsRepository.getPayrollByCountry()
                .stream()
                .map(result -> new CountryPayrollResponseDto(
                        result.getCountry(),
                        result.getTotalEmployees(),
                        result.getTotalPayrollUsd()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartmentPayrollResponseDto> getPayrollByDepartment() {
        return analyticsRepository.getPayrollByDepartment()
                .stream()
                .map(result -> new DepartmentPayrollResponseDto(
                        result.getDepartment(),
                        result.getTotalEmployees(),
                        result.getTotalPayrollUsd()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public SalaryStatisticsProjection getSalaryStatistics() {
        return analyticsRepository.getSalaryStatistics();
    }

    @Transactional(readOnly = true)
    public List<SalaryDistributionResponseDto> getSalaryDistribution() {
        return analyticsRepository.getSalaryDistribution()
                .stream()
                .map(result -> new SalaryDistributionResponseDto(
                        result.getSalaryRange(),
                        result.getEmployeeCount()
                ))
                .toList();
    }
}
