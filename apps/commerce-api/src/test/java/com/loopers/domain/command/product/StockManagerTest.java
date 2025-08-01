package com.loopers.domain.command.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.product.StockManager;
import com.loopers.domain.command.fixture.ProductFixture;
import com.loopers.environment.annotations.UnitTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@UnitTest
class StockManagerTest {

    private final StockManager stockManager = new StockManager();

    @DisplayName("재고 관리 기능 테스트")
    @Nested
    class StockManagement {

        @DisplayName("재고 차감 시, 주문 항목이 비어있는 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullAndEmptySource
        void throwsException_whenOrderItemsIsEmpty(Map<Long, Long> invalidOrderItems) {
            List<Product> products = List.of(ProductFixture.builder().build());

            assertThatThrownBy(() -> stockManager.deduct(invalidOrderItems, products))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("재고 차감 시, 상품 목록이 비어있는 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullAndEmptySource
        void throwsException_whenProductsIsEmpty(List<Product> invalidProducts) {
            Map<Long, Long> orderItems = Map.of(1L, 10L);

            assertThatThrownBy(() -> stockManager.deduct(orderItems, invalidProducts))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("재고 차감 시, 주문 항목에 해당하는 상품의 재고를 감소시킨다.")
        @Test
        void deductStock_whenOrderItemsAndProductsAreValid() {
            Map<Long, Long> orderItems = Map.of(
                1L, 5L,
                2L, 3L
            );

            List<Product> products = List.of(
                ProductFixture.builder().withId(1L).withStock(15L).build(),
                ProductFixture.builder().withId(2L).withStock(10L).build()
            );

            stockManager.deduct(orderItems, products);

            assertThat(products.get(0).getStock().count()).isEqualTo(10L);
            assertThat(products.get(1).getStock().count()).isEqualTo(7L);
        }
    }
}
