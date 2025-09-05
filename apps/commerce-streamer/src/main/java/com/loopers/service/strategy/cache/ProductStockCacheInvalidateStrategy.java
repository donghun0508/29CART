package com.loopers.service.strategy.cache;

import com.loopers.service.CacheInvalidate;
import com.loopers.service.PayloadMapper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductStockCacheInvalidateStrategy implements CacheInvalidateStrategy {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PayloadMapper payloadMapper;

    @Override
    public boolean supports(CacheInvalidate cacheInvalidateType) {
        return "OrderCreatedEvent".equals(cacheInvalidateType.getType());
    }

    @Override
    public void delete(CacheInvalidate cacheInvalidate) {
        String key = key(cacheInvalidate);
        if (key != null) {
            deleteByPrefix(key);
        }
    }

    private String key(CacheInvalidate cacheInvalidate) {
        ProductStockDecreasedEvent productStockDecreasedEvent = payloadMapper.parsePayload(cacheInvalidate.getPayload(),
            ProductStockDecreasedEvent.class);
        Map<Long, Long> stock = productStockDecreasedEvent.stock();
        Long stockCount = stock.get(0L);
        if (stockCount == 0) {
            return "product:detail";
        }
        return null;
    }

    private void deleteByPrefix(String prefix) {
        Set<String> keysToDelete = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(1000).build();

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                while (cursor.hasNext()) {
                    byte[] rawKey = cursor.next();
                    String key = (String) redisTemplate.getKeySerializer().deserialize(rawKey);
                    if (key != null) {
                        keysToDelete.add(key);
                    }
                }
            }
            return null;
        });

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }
    }

    public record ProductStockDecreasedEvent(Map<Long, Long> stock) {

    }
}
