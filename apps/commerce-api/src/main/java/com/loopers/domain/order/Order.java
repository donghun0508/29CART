package com.loopers.domain.order;


import com.loopers.domain.order.OrderEvent.OrderAppliedCouponEvent;
import com.loopers.domain.order.OrderEvent.OrderCompletedEvent;
import com.loopers.domain.order.OrderEvent.OrderCreatedEvent;
import com.loopers.domain.order.OrderEvent.OrderFailedEvent;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.shared.AggregateRoot;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.OrderCoupon;
import com.loopers.domain.shared.StockReservations.StockReservation;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "orders",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_ORDER_IDEMPOTENCY_KEY", columnNames = {"idempotency_key"})
    }
)
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

    @AttributeOverride(name = "value", column = @Column(name = "total_price"))
    private Money totalAmount;

    @AttributeOverrides({
        @AttributeOverride(name = "issuedCouponId", column = @Column(name = "issued_coupon_id")),
        @AttributeOverride(name = "paidPrice.value", column = @Column(name = "paid_price", nullable = false))
    })
    private OrderCoupon orderCoupon;

    @AttributeOverride(name = "number", column = @Column(name = "order_number", nullable = false, updatable = false))
    private OrderNumber orderNumber;

    @AttributeOverride(name = "key", column = @Column(name = "idempotency_key", nullable = false, updatable = false, unique = true))
    private IdempotencyKey idempotencyKey;

    @Transient
    private PaymentMethod paymentMethod;

    public static Order create(Long buyerId, IdempotencyKey idempotencyKey, OrderCoupon orderCoupon, List<StockReservation> stockReservations, PaymentMethod paymentMethod) {
        Order order = new Order();
        order.buyerId = buyerId;
        order.orderLines = OrderLines.of(order, stockReservations);
        order.status = OrderStatus.PENDING;
        order.orderNumber = OrderNumber.generate();
        order.orderCoupon = orderCoupon;
        order.idempotencyKey = idempotencyKey;
        order.paymentMethod = paymentMethod;
        order.totalAmount = order.orderLines.calculateTotalAmount();
        order.registerEvent(new OrderCreatedEvent(order));

        if(order.orderCoupon.hasCoupon()) {
            order.registerEvent(new OrderAppliedCouponEvent(order));
        }
        return order;
    }

    public void complete() {
        validateCompletable();
        this.status = OrderStatus.COMPLETED;
        this.registerEvent(new OrderCompletedEvent(this));
    }

    public void fail() {
        validateFailable();
        this.status = OrderStatus.FAILED;
        this.registerEvent(new OrderFailedEvent(this));
    }

    public boolean hasCoupon() {
        return this.orderCoupon.hasCoupon();
    }

    public Money paidAmount() {
        if (this.orderCoupon.hasCoupon()) {
            return this.orderCoupon.paidPrice();
        }
        return this.totalAmount;
    }

    public Long issuedCouponId() {
        return this.orderCoupon.issuedCouponId();
    }

    public List<Long> purchaseProductIds() {
        return this.orderLines.getLines().stream().map(OrderLine::getProductId).toList();
    }

    public Map<Long, Long> purchaseProducts() {
        return this.orderLines.getLines().stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    OrderLine::getProductId,
                    l -> l.getQuantity().count(),
                    Long::sum
                )
            );
    }

    private void validateCompletable() {
        if (!isPending()) {
            throw new IllegalStateException("Order.validateCompletable(): 이미 주문이 완료되었거나 취소된 주문입니다. 주문번호: " + this.orderNumber.number());
        }
    }

    private void validateFailable() {
        if(isFailed()) {
            throw new IllegalStateException("Order.validateFailable(): 이미 주문이 실패 상태입니다. 주문번호: " + this.orderNumber.number());
        }
    }

    private boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    private boolean isFailed() {
        return this.status == OrderStatus.FAILED;
    }
}
