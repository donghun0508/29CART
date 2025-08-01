package com.loopers.domain.command.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.domain.command.order.OrderCommand;
import com.loopers.domain.command.fixture.OrderCommandFixture;
import com.loopers.domain.command.fixture.TestArgumentProvider;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import com.loopers.environment.annotations.UnitTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

@UnitTest
class OrderCommandTest {

    @DisplayName("주문 요청 생성 시")
    @Nested
    class Create {

        @DisplayName("회원 Id가 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsException_whenUserIdIsNull(Long invalidUserId) {
            assertThatThrownBy(() -> new OrderCommand.Create(invalidUserId, List.of()))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("주문 항목이 비어 있는 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsException_whenItemsIsNull(List<OrderItem> invalidItems) {
            assertThatThrownBy(() -> new OrderCommand.Create(1L, invalidItems))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("주문 항목이 비어 있는 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @EmptySource
        void throwsException_whenItemsIsEmpty(List<OrderItem> invalidItems) {
            assertThatThrownBy(() -> new OrderCommand.Create(1L,invalidItems))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목의 상품 ID가 null 또는 0인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @MethodSource("generateZeroOrNull")
        void throwsException_whenProductIdIsNullOrZero(Long invalidProductId) {
            assertThatThrownBy(() -> OrderItem.builder()
                .productId(invalidProductId)
                .price(1000L)
                .quantity(1000L)
                .build())
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
        }

        @DisplayName("주문 항목의 가격이 null 또는 0인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @MethodSource("generateZeroOrNull")
        void throwsException_whenPriceIsNullOrZero(Long invalidPrice) {
            assertThatThrownBy(() -> OrderItem.builder()
                .productId(1L)
                .price(invalidPrice)
                .quantity(1000L)
                .build())
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
        }

        @DisplayName("주문 항목의 수량이 null 또는 0인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @MethodSource("generateZeroOrNull")
        void throwsException_whenQuantityIsNullOrZero(Long invalidQuantity) {
            assertThatThrownBy(() -> OrderItem.builder()
                .productId(1L)
                .price(1000L)
                .quantity(invalidQuantity)
                .build())
                .isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
        }

        @DisplayName("주문 명령 객체 생성 후 구매항목 목록을 비교한다.")
        @ParameterizedTest
        @MethodSource("generateRandomCount")
        void createOrderCommandAndCompareItems(int count) {
            OrderCommand.Create command = OrderCommandFixture.Create.builder().withOrderItems(count).build();
            long totalPrice = command.items().stream()
                .mapToLong(OrderItem::totalPrice)
                .sum();

            assertThat(command.items().size()).isEqualTo(count);
            assertThat(command.productIds().size()).isEqualTo(count);
            assertThat(command.totalPrice()).isEqualTo(totalPrice);
        }

        static Stream<Long> generateZeroOrNull() {
            return TestArgumentProvider.generateZeroOrNull();
        }

        static Stream<Integer> generateRandomCount() {
            return TestArgumentProvider.generateRandomCount();
        }

    }

}
