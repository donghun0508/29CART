package com.loopers.service.strategy.ranking;

import com.loopers.domain.ProductMetricEvent.ProductMetricType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotProductScoreCalculatorService {

    private final WeightProvider weightProvider;

    public double calculate(ProductMetricType type, Long score) {
        ScoreWeight weight = weightProvider.current();
        return weight.calculate(type, score);
    }
}
