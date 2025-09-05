package com.loopers.domain.catalog;


import com.loopers.common.domain.DomainEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class ProductEvent {

    @Getter
    public static class ProductHeartIncreasedEvent implements DomainEvent {

        private final Long productId;
        private final Long heartCount;

        public ProductHeartIncreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
        }
    }

    @Getter
    public static class ProductHeartDecreasedEvent implements DomainEvent {

        private final Long productId;
        private final Long heartCount;

        public ProductHeartDecreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
        }
    }

    @Getter
    public static class ProductDetailViewedEvent implements DomainEvent {
        private final String type = "detail_viewed";
        private final Long productId;

        public ProductDetailViewedEvent(Long productId) {
            this.productId = productId;
        }
    }

    @Getter
    public static class ProductStockRestoredEvent implements DomainEvent {
        private final Map<Long, Long> restoredStockMap;

        public ProductStockRestoredEvent(Product product, Stock restoredStock) {
            this.restoredStockMap = new HashMap<>();
            this.restoredStockMap.put(product.getId(), restoredStock.count());
        }
    }

    @Getter
    public static class ProductStockDecreasedEvent implements DomainEvent {
        private final Map<Long, Long> decreasedStockMap;

        public ProductStockDecreasedEvent(Product product) {
            this.decreasedStockMap = new HashMap<>();
            this.decreasedStockMap.put(product.getId(), product.getStock().count());
        }
    }

}
