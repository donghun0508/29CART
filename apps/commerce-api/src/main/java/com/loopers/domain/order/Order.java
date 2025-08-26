package com.loopers.domain.order;


import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.shared.AggregateRoot;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AggregateRoot {

    @Column(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    private Long buyerId;

    @Embedded
    private OrderLines orderLines;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @AttributeOverrides({
        @AttributeOverride(name = "issuedCouponId", column = @Column(name = "issued_coupon_id")),
        @AttributeOverride(name = "totalPrice.value", column = @Column(name = "total_price", nullable = false)),
        @AttributeOverride(name = "paidPrice.value", column = @Column(name = "paid_price", nullable = false))
    })
    private OrderPayment orderPayment;

    @AttributeOverride(name = "number", column = @Column(name = "order_number", nullable = false, updatable = false))
    private OrderNumber orderNumber;

    @AttributeOverride(name = "key", column = @Column(name = "idempotency_key", nullable = false, updatable = false, unique = true))
    private IdempotencyKey idempotencyKey;

    public static Order from(Long buyerId, IdempotencyKey idempotencyKey, List<OrderItem> items) {
        Order order = new Order();
        order.buyerId = buyerId;
        order.orderLines = OrderLines.of(order, items);
        order.status = OrderStatus.PENDING;
        order.orderPayment = OrderPayment.before(order.orderLines.calculateTotalAmount());
        order.orderNumber = OrderNumber.generate();
        order.idempotencyKey = idempotencyKey;
        return order;
    }

    public void applyCoupon(IssuedCoupon coupon) {
        Money payAmount = coupon.use(this.buyerId, this.paidAmount());
        this.orderPayment = OrderPayment.after(coupon.getId(), this.orderPayment.totalPrice(), payAmount);
    }

    public void processing() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("주문 상태가 결제 진행 가능한 상태가 아닙니다.");
        }

        this.status = OrderStatus.PROCESSING;
    }

    public void complete() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("주문 상태가 결제 가능한 상태가 아닙니다.");
        }

        this.status = OrderStatus.COMPLETED;
    }

    public void fail() {
        if (this.status == OrderStatus.FAILED) {
            throw new IllegalStateException("이미 주문이 실패 상태입니다.");
        }
        this.status = OrderStatus.FAILED;
    }

    public Money paidAmount() {
        return this.orderPayment.paidPrice();
    }

    public boolean canBePaid() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean isFailed() {
        return this.status == OrderStatus.FAILED;
    }

    public Long getCouponId() {
        return this.orderPayment.issuedCouponId();
    }

    public boolean isCouponUsed() {
        return false;
    }
}
