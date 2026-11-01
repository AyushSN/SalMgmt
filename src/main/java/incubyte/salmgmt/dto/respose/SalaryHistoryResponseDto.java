package incubyte.salmgmt.dto.respose;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SalaryHistoryResponseDto(

    Long id,
    BigDecimal oldAmount,
    BigDecimal newAmount,
    String currency,
    LocalDate effectiveDate,
    String reason,
    LocalDateTime createdAt
){}
