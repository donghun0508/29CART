package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.order.IdempotencyKey;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderLine;
import com.loopers.domain.order.OrderLines;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.shared.OrderCoupon;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class OrderFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final InstancioApi<Order> api;

        public Builder() {
            this.api = Instancio.of(Order.class)
                .set(field(Order::getOrderLines),
                    Instancio.of(OrderLines.class)
                        .generate(field(OrderLines::getLines), gen -> gen.collection()
                            .minSize(1)
                            .maxSize(10)
                        )
                        .create()
                );
        }

        public Builder buyerId(Long buyerId) {
            this.api.set(field(Order::getBuyerId), buyerId);
            return this;
        }

        public Builder orderItems(int size) {
            List<OrderLine> orderLines = Instancio.ofList(OrderLine.class).size(size).create();

            this.api.set(field(Order::getOrderLines),
                Instancio.of(OrderLines.class)
                    .set(field(OrderLines::getLines), orderLines)
                    .create()
            );
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.api.set(field(Order::getStatus), orderStatus);
            return this;
        }

        public Builder totalAmount(Long totalAmount) {
            this.api.set(field(Order::getTotalAmount), totalAmount);
            return this;
        }

        public Builder orderCoupon(OrderCoupon orderCoupon) {
            this.api.set(field(Order::getOrderCoupon), orderCoupon);
            return this;
        }

        public Builder orderNumber(String orderNumber) {
            this.api.set(field(Order::getOrderNumber), orderNumber);
            return this;
        }

        public Builder idempotencyKey(IdempotencyKey idempotencyKey) {
            this.api.set(field(Order::getIdempotencyKey), idempotencyKey);
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.api.set(field(Order::getPaymentMethod), paymentMethod);
            return this;
        }

        public Order build() {
            return api.create();
        }

    }

}
