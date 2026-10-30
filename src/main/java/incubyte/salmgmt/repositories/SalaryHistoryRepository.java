package incubyte.salmgmt.repositories;

import incubyte.salmgmt.entity.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory,Long> {

    List<SalaryHistory> findAllByEmployee_IdOrderByEffectiveDateDesc(Long id);

}
