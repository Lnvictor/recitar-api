package ccb.smonica.recitar_api.tasks;

import ccb.smonica.recitar_api.service.CacheService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Log4j2
public class CacheTasks {
    private CacheService cacheService;

    @Scheduled(fixedDelay = 7, initialDelay = 1,timeUnit = TimeUnit.DAYS)
    public void evictCache(){
        log.info("Its Sunday, cleaning the cache");
        this.cacheService.evictAllCacheValues();
    }
}
