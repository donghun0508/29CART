package com.loopers.service.strategy.cache;

import com.loopers.service.CacheInvalidate;

public interface CacheInvalidateStrategy {

    boolean supports(CacheInvalidate cacheInvalidateType);

    void delete(CacheInvalidate cacheInvalidate);
}
