package ccb.smonica.recitar_api.repository;

import ccb.smonica.recitar_api.entities.RecitativosCount;
import ccb.smonica.recitar_api.entities.YouthCult;
import org.springframework.data.repository.CrudRepository;

public interface RecitativosCountRepository extends CrudRepository<RecitativosCount, Integer> {
    RecitativosCount findByYouthCult(YouthCult youthCult);
}
