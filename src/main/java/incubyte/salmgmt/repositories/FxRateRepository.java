package incubyte.salmgmt.repositories;

import incubyte.salmgmt.entity.FxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FxRateRepository extends JpaRepository<FxRate,Long> {
}
