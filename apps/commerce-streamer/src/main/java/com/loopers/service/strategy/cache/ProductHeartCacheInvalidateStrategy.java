package com.loopers.service.strategy.cache;

import com.loopers.service.CacheInvalidate;
import com.loopers.service.PayloadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductHeartCacheInvalidateStrategy implements CacheInvalidateStrategy {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(CacheInvalidate cacheInvalidateType) {
        return "ProductHeartDecreasedEvent".equals(cacheInvalidateType.getType())
            || "ProductHeartIncreasedEvent".equals(cacheInvalidateType.getType());
    }

    @Override
    public void delete(CacheInvalidate cacheInvalidate) {
        String key = key(cacheInvalidate);
        redisTemplate.delete(key);
    }

    private String key(CacheInvalidate cacheInvalidate) {
        ProductHeart productHeart = payloadMapper.parsePayload(cacheInvalidate.getPayload(), ProductHeart.class);
        return "product:detail:" + productHeart.productId;
    }

    public record ProductHeart(Long productId, Long heartCount) {

    }
}
