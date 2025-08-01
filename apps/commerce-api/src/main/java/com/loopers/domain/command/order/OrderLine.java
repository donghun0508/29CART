package com.loopers.domain.command.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import com.loopers.domain.command.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order_line")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"order"})
public class OrderLine extends BaseEntity {

    @Column(
        name = "product_id",
        nullable = false,
        updatable = false
    )
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ORDER_ID"),
        nullable = false,
        updatable = false
    )
    private Order order;

    @AttributeOverride(name = "count", column = @Column(name = "quantity"))
    private Quantity quantity;

    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    static OrderLine createItem(Order order, OrderItem orderItem) {
        if (orderItem.quantity() <= 0) {
            throw new IllegalArgumentException("주문 수량이 0보다 작을 순 없습니다.");
        }

        OrderLine orderLine = new OrderLine();
        orderLine.order = order;
        orderLine.productId = orderItem.productId();
        orderLine.price = new Money(orderItem.price());
        orderLine.quantity = new Quantity(orderItem.quantity());

        return orderLine;
    }

    boolean hasSameProduct(OrderLine other) {
        return Objects.equals(this.productId, other.productId);
    }

    Money calculateLineTotal() {
        return this.price.multiply(this.quantity.count());
    }

    public Map.Entry<Long, Long> toPickedItemEntry() {
        return Map.entry(this.productId, this.quantity.count());
    }
}
