package com.loopers.service;

import com.loopers.service.strategy.cache.CacheInvalidateTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CacheInvalidateService {

    private final CacheInvalidateTemplate cacheInvalidateTemplate;

    public void invalidate(CacheInvalidate cacheInvalidateType) {
        cacheInvalidateTemplate.invalidate(cacheInvalidateType);

    }
}
