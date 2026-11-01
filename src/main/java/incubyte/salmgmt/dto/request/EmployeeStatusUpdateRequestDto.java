package incubyte.salmgmt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmployeeStatusUpdateRequestDto(
        @NotBlank(message = "Status is required")
        String status
) {}
