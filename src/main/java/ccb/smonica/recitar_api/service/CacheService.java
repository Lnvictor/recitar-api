package ccb.smonica.recitar_api.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CacheService {
    private CacheManager cacheManager;

    public void evictAllCacheValues() {
        Objects.requireNonNull(cacheManager.getCache("cults")).clear();
    }
}
