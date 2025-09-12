package com.loopers.service;

import com.loopers.domain.ProductMetricEvent.ProductMetricType;
import com.loopers.repository.HotProductListRepository;
import com.loopers.service.strategy.ranking.HotProductKeyGeneratorService;
import com.loopers.service.strategy.ranking.HotProductScoreCalculatorService;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductMetricAggregate {

    private final HotProductScoreCalculatorService hotProductScoreCalculatorService;
    private final HotProductKeyGeneratorService hotProductKeyGeneratorService;
    private final HotProductListRepository hotProductListRepository;
    private static final Duration TTL = Duration.ofDays(2);

    public void aggregate(Long productId, ProductMetricType type, Long score) {
        String key = hotProductKeyGeneratorService.generate();
        Map<Long, Double> deltaByProduct = new HashMap<>();
        double delta = hotProductScoreCalculatorService.calculate(type, score);
        deltaByProduct.merge(productId, delta, Double::sum);

        hotProductListRepository.applyRankingScores(key, deltaByProduct, TTL);
    }

}
