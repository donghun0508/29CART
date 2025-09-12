package com.loopers.service.strategy.ranking;

import com.loopers.service.strategy.ranking.HotProductKeyGenerator.HotProductKeyScope;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RedisScopeProvider implements ScopeProvider {

    private final StringRedisTemplate redis;

    private static final String KEY = "metric:scope";
    private static final long REFRESH_INTERVAL_MS = 5000L;

    private static final HotProductKeyScope DEF_SCOPE = HotProductKeyScope.DAILY;

    private final AtomicLong lastReloadAt = new AtomicLong(0L);
    private final AtomicReference<HotProductKeyScope> ref = new AtomicReference<>(DEF_SCOPE);

    @PostConstruct
    public void init() {
        safeRefresh();
    }

    @Override
    public HotProductKeyScope current() {
        long now = System.currentTimeMillis();
        long last = lastReloadAt.get();

        if (now - last > REFRESH_INTERVAL_MS && lastReloadAt.compareAndSet(last, now)) {
            safeRefresh();
        }
        return ref.get();
    }

    private void safeRefresh() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("Failed to refresh scope from Redis, keeping previous scope: {}", ref.get(), e);
        }
    }

    private void refresh() {
        String raw = redis.opsForValue().get(KEY);

        if (raw != null && !raw.isBlank()) {
            HotProductKeyScope scope = HotProductKeyScope.valueOf(raw.trim());
            ref.set(scope);
            return;
        }

        writeToRedis(DEF_SCOPE);
        ref.set(DEF_SCOPE);
    }

    private void writeToRedis(HotProductKeyScope scope) {
        redis.opsForValue().set(KEY, scope.name());
    }
}
