package incubyte.salmgmt.dto.respose;

public record SalaryDistributionResponseDto(
        String salaryRange,
        Long employeeCount
){}
