package com.loopers.domain.order;


import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.Money;

public class OrderEvent {

    public record OrderCreatedEvent(Long buyerId, Money paidAmount, OrderNumber orderNumber) implements DomainEvent {

        public OrderCreatedEvent(Order order) {
            this(order.getBuyerId(), order.paidAmount(), order.getOrderNumber());
        }
    }

}
