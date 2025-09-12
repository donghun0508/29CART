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
        private final Long unitPrice;

        public ProductHeartIncreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
            this.unitPrice = product.getUnitPrice().value();
        }
    }

    @Getter
    public static class ProductHeartDecreasedEvent implements DomainEvent {

        private final Long productId;
        private final Long heartCount;
        private final Long unitPrice;

        public ProductHeartDecreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
            this.unitPrice = product.getUnitPrice().value();
        }
    }

    @Getter
    public static class ProductDetailViewedEvent implements DomainEvent {
        private final Long productId;
        private final Long unitPrice;

        public ProductDetailViewedEvent(ProductRead productRead) {
            this.productId = productRead.getId();
            this.unitPrice = productRead.getUnitPrice();
        }
    }

    @Getter
    public static class ProductStockRestoredEvent implements DomainEvent {
        private final Long productId;
        private final Long restoredStock;
        private final Long unitPrice;

        public ProductStockRestoredEvent(Product product, Stock restoredStock) {
            this.productId = product.getId();
            this.restoredStock = restoredStock.count();
            this.unitPrice = product.getUnitPrice().value();
        }
    }

    @Getter
    public static class ProductStockDecreasedEvent implements DomainEvent {
        private final Long productId;
        private final Long decreasedStock;
        private final Long unitPrice;

        public ProductStockDecreasedEvent(Product product, Stock decreasedStock) {
            this.productId = product.getId();
            this.decreasedStock = decreasedStock.count();
            this.unitPrice = product.getUnitPrice().value();
        }
    }

}
