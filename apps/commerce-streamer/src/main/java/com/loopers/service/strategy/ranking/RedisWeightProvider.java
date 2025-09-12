package com.loopers.service.strategy.ranking;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RedisWeightProvider implements WeightProvider {

    private final StringRedisTemplate redis;

    private static final String KEY = "metric:weights";
    private static final String FIELD_VIEW = "view";
    private static final String FIELD_LIKE = "like";
    private static final String FIELD_PURCHASE = "purchase";

    private static final String DEF_VIEW = "0.1";
    private static final String DEF_LIKE = "0.2";
    private static final String DEF_PURCHASE = "0.7";

    private final AtomicReference<ScoreWeight> ref =
        new AtomicReference<>(new ScoreWeight(
            Double.parseDouble(DEF_VIEW),
            Double.parseDouble(DEF_LIKE),
            Double.parseDouble(DEF_PURCHASE)));

    private final AtomicLong lastReloadAt = new AtomicLong(0L);
    private static final long REFRESH_INTERVAL_MS = 5000L;

    @PostConstruct
    public void init() {
        safeRefresh();
    }

    @Override
    public ScoreWeight current() {
        long now = System.currentTimeMillis();
        long last = lastReloadAt.get();

        if (now - last > REFRESH_INTERVAL_MS && lastReloadAt.compareAndSet(last, now)) {
            safeRefresh();
        }
        return ref.get();
    }

    public void update(ScoreWeight w) {
        validate(w);
        writeToRedis(w);
        ref.set(w);
        lastReloadAt.set(System.currentTimeMillis());
    }

    private void safeRefresh() {
        try {
            refresh();
        } catch (Exception e) {
            log.warn("Failed to refresh weights from Redis, keeping previous weights: {}", ref.get(), e);
        }
    }

    private void refresh() {
        var m = redis.opsForHash().entries(KEY);

        if (!m.isEmpty()) {
            double v = parseOrDefault(m.get(FIELD_VIEW), DEF_VIEW);
            double l = parseOrDefault(m.get(FIELD_LIKE), DEF_LIKE);
            double p = parseOrDefault(m.get(FIELD_PURCHASE), DEF_PURCHASE);
            ref.set(new ScoreWeight(v, l, p));
            log.debug("Weights refreshed from Redis: {}", ref.get());
        } else {
            ScoreWeight defaults = new ScoreWeight(
                Double.parseDouble(DEF_VIEW),
                Double.parseDouble(DEF_LIKE),
                Double.parseDouble(DEF_PURCHASE)
            );
            writeToRedis(defaults);
            ref.set(defaults);
            log.warn("No weights found in Redis. Initialized with defaults: {}", defaults);
        }
    }

    private void writeToRedis(ScoreWeight w) {
        Map<String, String> payload = Map.of(
            FIELD_VIEW, String.valueOf(w.viewWeight()),
            FIELD_LIKE, String.valueOf(w.likeWeight()),
            FIELD_PURCHASE, String.valueOf(w.purchaseWeight())
        );
        redis.opsForHash().putAll(KEY, payload);
    }

    private double parseOrDefault(Object val, String def) {
        try {
            return Double.parseDouble(String.valueOf(val != null ? val : def));
        } catch (NumberFormatException e) {
            log.warn("Invalid weight value '{}', falling back to default {}", val, def);
            return Double.parseDouble(def);
        }
    }

    private void validate(ScoreWeight w) {
        if (w.viewWeight() < 0 || w.likeWeight() < 0 || w.purchaseWeight() < 0) {
            throw new IllegalArgumentException("weights must be >= 0");
        }
    }
}
