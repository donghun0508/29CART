package com.loopers.service.strategy.ranking;

public interface HotProductKeyGenerator {

    boolean isSupport(HotProductKeyScope scope);

    String generate();
    String next();

    enum HotProductKeyScope {
        DAILY,
        HOURLY
    }
}
