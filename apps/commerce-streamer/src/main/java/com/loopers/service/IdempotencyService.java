package com.loopers.service;

import com.loopers.utils.HashUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean alreadyProcessed(String payload) {
        String hashKey = buildKey(payload);
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(hashKey, "1", Duration.ofSeconds(10));
        return Boolean.FALSE.equals(isNew);
    }

    private String buildKey(String payload) {
        return "idempotency:event:" + HashUtil.sha256(payload);
    }
}

