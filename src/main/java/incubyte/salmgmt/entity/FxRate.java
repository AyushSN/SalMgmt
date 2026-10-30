package incubyte.salmgmt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "fx_rate")
@Getter
@Setter
public class FxRate {

    @Id
    @Column(length = 3)
    private String currency;

    @Column(name = "rate_to_usd", nullable = false, precision = 19, scale = 8)
    private BigDecimal rateToUsd;
}
