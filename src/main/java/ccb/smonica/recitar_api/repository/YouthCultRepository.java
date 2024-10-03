package ccb.smonica.recitar_api.repository;

import ccb.smonica.recitar_api.entities.YouthCult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YouthCultRepository extends JpaRepository<YouthCult, Long> {
    List<YouthCult> findByYearAndMonth(String year, String month);
    YouthCult findByYearAndMonthAndDay(String year, String month, String day);
}
