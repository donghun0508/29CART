package com.loopers.service.strategy.ranking;

import com.loopers.service.strategy.ranking.HotProductKeyGenerator.HotProductKeyScope;

public interface ScopeProvider {
    HotProductKeyScope current();
}
