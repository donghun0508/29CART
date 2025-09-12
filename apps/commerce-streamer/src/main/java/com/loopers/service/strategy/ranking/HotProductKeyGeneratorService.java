package com.loopers.service.strategy.ranking;

import com.loopers.service.strategy.ranking.HotProductKeyGenerator.HotProductKeyScope;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HotProductKeyGeneratorService {

    private final ScopeProvider scopeProvider;
    private final List<HotProductKeyGenerator> hotProductKeyGenerators;

    public String generate() {
        HotProductKeyScope hotProductKeyScope = scopeProvider.current();

        for (HotProductKeyGenerator hotProductKeyGenerator : hotProductKeyGenerators) {
            if (hotProductKeyGenerator.isSupport(hotProductKeyScope)) {
                return hotProductKeyGenerator.generate();
            }
        }
        throw new IllegalStateException("No suitable HotProductKeyGenerator found for scope: " + hotProductKeyScope);
    }
}
