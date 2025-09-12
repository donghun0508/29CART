package com.loopers.service;

import com.loopers.utils.HashUtil;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<ProductMetricAnalysis> filterAlreadyProcessed(List<ProductMetricAnalysis> analyses) {
        return analyses.stream()
            .filter(analysis -> !alreadyProcessed(analysis.getEventId()))
            .toList();
    }

    private boolean alreadyProcessed(String eventId) {
        String hashKey = buildKey(eventId);
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(hashKey, "1", Duration.ofSeconds(5));
        return Boolean.FALSE.equals(isNew);
    }

    private String buildKey(String eventId) {
        return "idempotency:event:" + HashUtil.sha256(eventId);
    }
}

