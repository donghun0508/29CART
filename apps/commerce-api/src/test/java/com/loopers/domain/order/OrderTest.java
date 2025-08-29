package com.loopers.domain.order;

import static com.loopers.domain.order.OrderStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.payment.PointMethod;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.StockReservations;
import com.loopers.domain.shared.StockReservations.StockReservation;
import com.loopers.fixture.OrderCouponFixture;
import com.loopers.fixture.OrderFixture;
import com.loopers.fixture.StockReservationFixture;
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
            var stockReservations = StockReservationFixture.builder().orderItems(5).build();
            var stockReservationCollection = StockReservations.from(stockReservations);
            var orderCoupon = OrderCouponFixture.builder().build();
            var idempotencyKey = IdempotencyKey.generate();

            Order order = Order.create(buyerId, idempotencyKey, orderCoupon, stockReservations, new PointMethod());

            assertThat(order).isNotNull();
            assertThat(order.getBuyerId()).isEqualTo(buyerId);
            assertThat(order.getIdempotencyKey()).isEqualTo(idempotencyKey);
            assertThat(order.getOrderCoupon()).isEqualTo(orderCoupon);
            assertThat(order.getStatus()).isEqualTo(PENDING);
            assertThat(order.getOrderLines().getLines())
                .hasSize(stockReservations.size())
                .zipSatisfy(stockReservations, (orderLine, productItem) -> {
                    assertThat(orderLine.getProductId()).isEqualTo(productItem.productId());
                    assertThat(orderLine.getPrice()).isEqualTo(productItem.price());
                    assertThat(orderLine.getQuantity()).isEqualTo(productItem.quantity());
                });
            assertThat(order.getTotalAmount()).isEqualTo(stockReservationCollection.totalPrice());
            assertThat(order.hasCoupon()).isEqualTo(orderCoupon.hasCoupon());
            assertThat(order.getPaymentMethod().getClass()).isEqualTo(PointMethod.class);
        }

        @DisplayName("유효한 주문 생성 명령을 전달한 경우, 주문 도메인의 총 금액이 올바르게 계산된다.")
        @Test
        void calculateTotalPrice() {
            var buyerId = 1L;
            var orderItems = StockReservationFixture.builder().orderItems(5).build();
            var orderCoupon = OrderCouponFixture.builder().issuedCouponId(null).build();

            Order order = Order.create(buyerId, IdempotencyKey.generate(), orderCoupon, orderItems, new PointMethod());

            Money expectedTotalPrice = orderItems.stream().map(StockReservation::totalPrice).reduce(Money.ZERO, Money::add);

            assertThat(order.paidAmount()).isEqualTo(expectedTotalPrice);
        }
    }

    @DisplayName("주문 도메인 상태 변경 시, ")
    @Nested
    class Status {

        @DisplayName("주문 완료 시, 이미 완료된 주문인 경우 예외를 발생시킨다.")
        @Test
        void throwsExceptionWhenCompleteAlreadyCompletedOrder() {
            var order = OrderFixture.builder().orderStatus(OrderStatus.COMPLETED).build();

            assertThatThrownBy(order::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order.validateCompletable()")
                .hasMessageContaining(order.getOrderNumber().number())
            ;
        }

        @DisplayName("주문 완료 시, 주문이 완료 상태로 변경된다.")
        @Test
        void completeOrder() {
            var order = OrderFixture.builder().orderStatus(PENDING).build();

            order.complete();

            assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        }

        @DisplayName("주문 취소 시, 이미 취소된 주문인 경우 예외를 발생시킨다.")
        @Test
        void throwsExceptionWhenFailAlreadyFailedOrder() {
            var order = OrderFixture.builder().orderStatus(OrderStatus.FAILED).build();

            assertThatThrownBy(order::fail)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Order.validateFailable()")
                .hasMessageContaining(order.getOrderNumber().number())
            ;
        }

        @DisplayName("주문 취소 시, 주문이 취소 상태로 변경된다.")
        @Test
        void failOrder() {
            var order = OrderFixture.builder().orderStatus(PENDING).build();

            order.fail();

            assertThat(order.getStatus()).isEqualTo(OrderStatus.FAILED);
        }
    }
}
