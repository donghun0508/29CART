package com.loopers.domain;

import com.loopers.common.domain.DomainEvent;
import lombok.Getter;

public class ProductMetricEvent {

    public enum ProductMetricType {
        PRODUCT_INCREMENTED_LIKE,
        PRODUCT_DECREMENTED_LIKE,
        PRODUCT_INCREMENTED_VIEW,
        PRODUCT_INCREASED_STOCK,
        PRODUCT_DECREMENTED_STOCK
    }

    public interface ProductMetricEventMarker extends DomainEvent {
        Long getProductId();
        ProductMetricType getType();
        Long getScore();
    }

    @Getter
    public static class ProductIncrementedViewEvent implements ProductMetricEventMarker {
        private final Long productId;
        private final Long viewCount;
        private final Long score;
        private final ProductMetricType type = ProductMetricType.PRODUCT_INCREMENTED_VIEW;

        public ProductIncrementedViewEvent(ProductMetric productMetric, Long score) {
            this.productId = productMetric.getProductId();
            this.viewCount = productMetric.getViewCount();
            this.score = score;
        }
    }

    @Getter
    public static class ProductIncrementedLikeEvent implements ProductMetricEventMarker {
        private final Long productId;
        private final Long likeCount;
        private final Long score;
        private final ProductMetricType type = ProductMetricType.PRODUCT_INCREMENTED_LIKE;

        public ProductIncrementedLikeEvent(ProductMetric productMetric, Long score) {
            this.productId = productMetric.getProductId();
            this.likeCount = productMetric.getLikeCount();
            this.score = score;
        }
    }

    @Getter
    public static class ProductDecrementedLikeEvent implements ProductMetricEventMarker {
        private final Long productId;
        private final Long likeCount;
        private final Long score;
        private final ProductMetricType type = ProductMetricType.PRODUCT_DECREMENTED_LIKE;

        public ProductDecrementedLikeEvent(ProductMetric productMetric, Long score) {
            this.productId = productMetric.getProductId();
            this.likeCount = productMetric.getLikeCount();
            this.score = score;
        }
    }

    @Getter
    public static class ProductIncreasedStockEvent implements ProductMetricEventMarker {
        private final Long productId;
        private final Long unitPrice;
        private final Long increasedStock;
        private final Long score;
        private final ProductMetricType type = ProductMetricType.PRODUCT_INCREASED_STOCK;

        public ProductIncreasedStockEvent(ProductMetric productMetric, Long increasedStock, Long score) {
            this.productId = productMetric.getProductId();
            this.unitPrice = productMetric.getUnitPrice();
            this.increasedStock = increasedStock;
            this.score = score;
        }
    }

    @Getter
    public static class ProductDecrementedStockEvent implements ProductMetricEventMarker {
        private final Long productId;
        private final Long unitPrice;
        private final Long decreasedStock;
        private final Long score;
        private final ProductMetricType type = ProductMetricType.PRODUCT_DECREMENTED_STOCK;

        public ProductDecrementedStockEvent(ProductMetric productMetric, Long decreasedStock, Long score) {
            this.productId = productMetric.getProductId();
            this.unitPrice = productMetric.getUnitPrice();
            this.decreasedStock = decreasedStock;
            this.score = score;
        }
    }
}
