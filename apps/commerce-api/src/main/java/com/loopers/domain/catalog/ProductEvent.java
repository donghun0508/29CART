package com.loopers.domain.catalog;

import com.loopers.domain.shared.DomainEvent;
import com.loopers.logging.notification.NotificationEvent;

public class ProductEvent {

    public static class ProductRestoredEvent extends DomainEvent implements NotificationEvent {
        private final String orderNumber;

        public ProductRestoredEvent(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        @Override
        public String message() {
            return String.format("결제가 취소되어 재고를 복구합니다. [주문번호: %s]", orderNumber);
        }
    }

    public static class ProductHeartIncreasedEvent extends DomainEvent implements NotificationEvent{
        private final Long productId;
        private final Long heartCount;

        public ProductHeartIncreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
        }

        @Override
        public String message() {
            return String.format("상품 좋아요 수가 증가했습니다. [상품ID: %d] [좋아요: %d개]", productId, heartCount);
        }
    }

    public static class ProductHeartDecreasedEvent extends DomainEvent implements NotificationEvent {
        private final Long productId;
        private final Long heartCount;

        public ProductHeartDecreasedEvent(Product product) {
            this.productId = product.getId();
            this.heartCount = product.getHeartCount();
        }

        @Override
        public String message() {
            return String.format("상품 좋아요 수가 감소했습니다. [상품ID: %d] [좋아요: %d개]", productId, heartCount);
        }
    }

}
