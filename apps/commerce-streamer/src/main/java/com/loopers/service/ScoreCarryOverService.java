package com.loopers.service;

import com.loopers.service.strategy.ranking.DailyHotProductKeyGenerator;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScoreCarryOverService {

    private static final long CARRY_OVER_COUNT = 100;
    private static final double CARRY_OVER_RATIO = 0.1; // 10%

    private final StringRedisTemplate redis;
    private final DailyHotProductKeyGenerator dailyHotProductKeyGenerator;

    public void carry() {
        String todayKey = dailyHotProductKeyGenerator.generate();
        String nextKey  = dailyHotProductKeyGenerator.next();

        redis.delete(nextKey);

        Set<ZSetOperations.TypedTuple<String>> entries =
            redis.opsForZSet().reverseRangeWithScores(todayKey, 0, CARRY_OVER_COUNT - 1);

        if (entries == null || entries.isEmpty()) {
            return;
        }

        byte[] nextKeyBytes = nextKey.getBytes(StandardCharsets.UTF_8);
        redis.executePipelined((RedisCallback<Object>) conn -> {
            for (ZSetOperations.TypedTuple<String> e : entries) {
                String member = e.getValue();
                Double score  = e.getScore();
                if (member == null || score == null) continue;

                double newScore = score * CARRY_OVER_RATIO;

                conn.zAdd(nextKeyBytes, newScore, member.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
    }
}
