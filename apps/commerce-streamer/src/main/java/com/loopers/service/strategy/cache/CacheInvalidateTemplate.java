package com.loopers.service.strategy.cache;

import com.loopers.service.CacheInvalidate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CacheInvalidateTemplate {

    private final List<CacheInvalidateStrategy> cacheInvalidateStrategies;

    public void invalidate(CacheInvalidate cacheInvalidate) {
        for (CacheInvalidateStrategy cacheInvalidateStrategy : cacheInvalidateStrategies) {
            if (cacheInvalidateStrategy.supports(cacheInvalidate)) {
                cacheInvalidateStrategy.delete(cacheInvalidate);
                return;
            }
        }
    }
}
