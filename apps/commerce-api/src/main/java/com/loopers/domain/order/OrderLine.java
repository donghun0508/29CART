package com.loopers.domain.order;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter(AccessLevel.PACKAGE)
@Entity
@Table(name = "order_line")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class OrderLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "order_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ORDER_ID"),
        nullable = false,
        updatable = false
    )
    private Order order;

    @Column(
        name = "product_id",
        nullable = false,
        updatable = false
    )
    private Long productId;

    @AttributeOverride(name = "value", column = @Column(name = "unit_price"))
    private Money price;

    @AttributeOverride(name = "count", column = @Column(name = "quantity"))
    private Quantity quantity;

    static OrderLine from(Order order, OrderItem orderItem) {
        requireNonNull(orderItem, "주문 항목은 null일 수 없습니다.");

        OrderLine orderLine = new OrderLine();
        orderLine.order = order;
        orderLine.productId = orderItem.productId();
        orderLine.price = orderItem.price();
        orderLine.quantity = orderItem.quantity();

        return orderLine;
    }

    Money calculateLineTotal() {
        return this.price.multiply(this.quantity.count());
    }
}
