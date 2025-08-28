package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.payment.PointMethod;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.StockReservations.StockReservation;
import com.loopers.fixture.OrderItemFixture;
import com.loopers.fixture.ProductItemFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderTest {

    @DisplayName("주문 도메인 생성 시, ")
    @Nested
    class Create {

        @DisplayName("유효한 주문 생성 명령을 전달하면 주문 도메인을 생성한다.")
        @Test
        void createOrder() {
            var buyerId = 1L;
            var reservations = ProductItemFixture.builder().productItems(5).build();

            Order order = Order.create(buyerId, IdempotencyKey.generate(), null, reservations, new PointMethod());

            assertThat(order).isNotNull();
            assertThat(order.getBuyerId()).isEqualTo(buyerId);

            assertThat(order.getOrderLines().getLines())
                .hasSize(reservations.size())
                .zipSatisfy(reservations, (orderLine, productItem) -> {
                    assertThat(orderLine.getProductId()).isEqualTo(productItem.productId());
                    assertThat(orderLine.getPrice()).isEqualTo(productItem.price());
                    assertThat(orderLine.getQuantity()).isEqualTo(productItem.quantity());
                });
        }

        @DisplayName("유효한 주문 생성 명령을 전달한 경우, 주문 도메인의 총 금액이 올바르게 계산된다.")
        @Test
        void calculateTotalPrice() {
            var buyerId = 1L;
            var orderItems = OrderItemFixture.builder().orderItems(5).build();

            Order order = Order.create(buyerId, IdempotencyKey.generate(), null, orderItems, new PointMethod());

            Money expectedTotalPrice = orderItems.stream().map(StockReservation::totalPrice).reduce(Money.ZERO, Money::add);

            assertThat(order.paidAmount()).isEqualTo(expectedTotalPrice);
        }
    }
}
