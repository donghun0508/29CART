package com.loopers.domain.command.order;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.domain.command.order.Order;
import com.loopers.domain.command.order.OrderCommand;
import com.loopers.domain.command.fixture.OrderCommandFixture;
import com.loopers.domain.command.fixture.TestArgumentProvider;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import com.loopers.environment.annotations.UnitTest;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@UnitTest
class OrderTest {

    @DisplayName("주문 객체 생성 시,")
    @Nested
    class Create {

        @DisplayName("회원 PK값이 유효한 경우 주문 객체를 생성한다.")
        @Test
        void createOrder_whenUserIdIsValid() {
            Long validUserId = 1L;
            OrderCommand.Create command = OrderCommandFixture.Create.builder().withUserId(validUserId).build();

            Order order = Order.create(command);

            assertThat(order).satisfies(o -> {
                assertThat(o).isNotNull();
                assertThat(o.getUserId()).isEqualTo(validUserId);
                assertThat(o.getOrderLines()).isNotNull();
                assertThat(o.getOrderLines().size()).isEqualTo(command.items().size());
                assertThat(o.getTotalAmount().value()).isEqualTo(command.totalPrice());
                assertThat(o)
                    .satisfies(item -> {
                        Map<Long, Long> lines = item.lines();
                        assertThat(lines.size()).isEqualTo(command.items().size());
                        for (OrderItem orderItem : command.items()) {
                            assertThat(lines.get(orderItem.productId())).isEqualTo(orderItem.quantity());
                        }
                    });
            });
        }
    }

    static Stream<Integer> generateRandomCount() {
        return TestArgumentProvider.generateRandomCount();
    }
}

/// ///
//package com.loopers.domain.order;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import com.loopers.domain.fixture.OrderFixture;
//import com.loopers.domain.fixture.PickingReportFixture;
//import com.loopers.domain.fixture.TestArgumentProvider;
//import com.loopers.domain.fixture.UserFixture;
//import com.loopers.domain.command.order.OrderCommand.Phase.PickedItem;
//import com.loopers.domain.user.User;
//import com.loopers.environment.annotations.UnitTest;
//import java.util.List;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.NullAndEmptySource;
//import org.junit.jupiter.params.provider.ValueSource;
//
//@UnitTest
//class OrderTest {
//
//    @DisplayName("주문 객체 생성 시,")
//    @Nested
//    class Create {
//
//
//        @DisplayName("회원 PK값이 null이 아닌 경우 주문 객체를 생성한다.")
//        @Test
//        void createOrder_whenUserIdIsValid() {
//            User user = UserFixture.builder().build();
//            OrderCommand.Create command = new OrderCommand.Create(user.getId());
//            Order order = Order.create(command);
//
//            assertThat(order).isNotNull();
//            assertThat(order.getUserId()).isEqualTo(user.getId());
//            assertThat(order.getOrderNumber()).isNotBlank();
//        }
//    }
//
//    @DisplayName("피킹 결과 적용 시,")
//    @Nested
//    class ApplyPickingResult {
//
//        @DisplayName("피킹 리포트가 null인 경우 예외가 발생한다.")
//        @Test
//        void throwException_whenPickingReportIsNull() {
//            Order order = OrderFixture.builder().build();
//
//            assertThatThrownBy(() -> order.applyPickingResult(null))
//                .isInstanceOf(NullPointerException.class);
//        }
//
//        @DisplayName("피킹 리포트에 피킹된 아이템이 없는 경우 예외가 발생한다.")
//        @ParameterizedTest
//        @NullAndEmptySource
//        void throwException_whenPickedItemsIsEmpty(List<PickedItem> invalidPickedItems) {
//            Order order = OrderFixture.builder().build();
//
//            PickingReport pickingReportRequestCommand = PickingReport.builder().pickedItems(invalidPickedItems).build();
//
//            assertThatThrownBy(() -> order.applyPickingResult(pickingReportRequestCommand))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("피킹 리포트의 주문 번호가 주문 객체의 주문 번호와 일치하지 않는 경우 예외가 발생한다.")
//        @Test
//        void throwException_whenOrderNumberDoesNotMatch() {
//            String originalOrderNumber = "ORD123456";
//            String mismatchedOrderNumber = "ORD654321";
//
//            Order order = OrderFixture.builder().withOrderNumber(originalOrderNumber).build();
//            PickingReport pickingReportRequestCommand = PickingReportFixture.builder()
//                .withOrderNumber(mismatchedOrderNumber)
//                .build();
//
//            assertThatThrownBy(() -> order.applyPickingResult(pickingReportRequestCommand))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("피킹 수량이 0 이하인 경우 예외가 발생한다.")
//        @ParameterizedTest
//        @ValueSource(
//            longs = {0L, -1L, -999L}
//        )
//        void throwException_whenPickingQuantityIsInvalid(Long invalidQuantity) {
//            Order order = OrderFixture.builder().build();
//            List<PickedItem> pickedItems = List.of(PickedItem.builder().productId(1L).price(1000L).quantity(invalidQuantity).build());
//            PickingReport pickingReportRequestCommand = PickingReport.builder().pickedItems(pickedItems).build();
//
//            assertThatThrownBy(() -> order.applyPickingResult(pickingReportRequestCommand))
//                .isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @DisplayName("동일한 상품에 대해 피킹을 중복 확정할 경우 예외가 발생한다.")
//        @Test
//        void throwException_whenConfirmingPickingForDuplicateProduct() {
//            Long duplicateProductId = 1L;
//            List<PickedItem> pickedItems = List.of(PickedItem.builder().productId(duplicateProductId).price(1000L).quantity(1000L).build());
//            Order order = OrderFixture.builder().build();
//            PickingReport pickingReportRequest1 = PickingReport.builder().orderNumber(order.getOrderNumber()).pickedItems(pickedItems).build();
//            PickingReport pickingReportRequest2 = PickingReport.builder().orderNumber(order.getOrderNumber()).pickedItems(pickedItems).build();
//            order.applyPickingResult(pickingReportRequest1);
//
//            assertThatThrownBy(() -> order.applyPickingResult(pickingReportRequest2))
//                .isInstanceOf(IllegalStateException.class);
//        }
//
//        @DisplayName("여러 개의 피킹 항목을 확정할 때 주문 라인 개수가 정확히 증가한다.")
//        @ParameterizedTest
//        @MethodSource("generateRandomCount")
//        void confirmPicking_increasesOrderLineCount(Integer pickedItemCount) {
//            Order order = OrderFixture.builder().withEmptyOrderLine().build();
//            PickingReport pickingReport = PickingReportFixture.builder()
//                .withOrderNumber(order.getOrderNumber())
//                .withPickedItems(pickedItemCount)
//                .build();
//
//            order.applyPickingResult(pickingReport);
//
//            assertThat(order.getOrderLineCount()).isEqualTo(pickedItemCount);
//        }
//
//        @DisplayName("여러 주문 항목 추가 시 총 금액이 정확히 계산된다.")
//        @ParameterizedTest
//        @MethodSource("generateRandomCount")
//        void calculateTotalAmount_whenOrderLinesAreAdded(Integer pickedItemCount) {
//            Order order = OrderFixture.builder().withEmptyOrderLine().build();
//            PickingReport pickingReport = PickingReportFixture.builder()
//                .withOrderNumber(order.getOrderNumber())
//                .withPickedItems(pickedItemCount)
//                .build();
//            order.applyPickingResult(pickingReport);
//            Long totalAmount = pickingReport.totalPrice();
//
//            assertThat(order.getTotalAmount().value()).isEqualTo(totalAmount);
//        }
//
//        static Stream<Integer> generateRandomCount() {
//            return TestArgumentProvider.generateRandomCount();
//        }
//
//    }
//}
