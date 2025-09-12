package com.loopers.repository;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HotProductListRepository {

    private final StringRedisTemplate redis;

    public void applyRankingScores(String key, Map<Long, Double> productDelta, Duration ttl) {
        if (productDelta.isEmpty()) {
            return;
        }

        redis.executePipelined((RedisCallback<Object>) connection -> {
            var ser = redis.getStringSerializer();
            byte[] k = ser.serialize(key);

            productDelta.forEach((productId, delta) ->
                connection
                    .zSetCommands()
                    .zIncrBy(Objects.requireNonNull(k),
                        delta, Objects.requireNonNull(ser.serialize(productId.toString()))));

            connection.keyCommands()
                .expire(Objects.requireNonNull(k), ttl.toSeconds());
            return null;
        });
    }
}
