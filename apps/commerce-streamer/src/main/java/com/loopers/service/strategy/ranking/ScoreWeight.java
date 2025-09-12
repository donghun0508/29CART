package com.loopers.service.strategy.ranking;


import com.loopers.domain.ProductMetricEvent.ProductMetricType;

public record ScoreWeight(
    double viewWeight,
    double likeWeight,
    double purchaseWeight
) {

    public double calculate(ProductMetricType type, Long score) {
        return switch (type) {
            case PRODUCT_INCREMENTED_VIEW -> score * viewWeight;
            case PRODUCT_INCREMENTED_LIKE, PRODUCT_DECREMENTED_LIKE -> score * likeWeight;
            case PRODUCT_INCREASED_STOCK, PRODUCT_DECREMENTED_STOCK -> score * purchaseWeight;
        };
    }
}
