package com.loopers.domain.command.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.fixture.ProductFixture;
import com.loopers.environment.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class ProductTest {

    @DisplayName("상품 도메인 재고 관련, ")
    @Nested
    class StockCommand {

        @DisplayName("재고 감소 시, 재고가 충분하지 않으면 예외를 발생시킨다.")
        @Test
        void throwsInvalidException_whenGenderIsNullAndEmpty() {
            long initialStock = 10L;
            Product product = ProductFixture.builder().withStock(initialStock).build();
            long decreaseStock = initialStock + 1L;

            assertThatThrownBy(() -> product.decreaseStock(decreaseStock))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("재고 감소 시, 재고가 null인 경우 예외를 발생시킨다.")
        @Test
        void throwsInvalidException_whenDecreaseStockWithNull() {
            Product product = ProductFixture.builder().build();

            assertThatThrownBy(() -> product.decreaseStock(null))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("재고 감소 시, 재고가 충분하면 정상적으로 재고를 감소시킨다.")
        @Test
        void success_whenStockIsSufficient() {
            long initialStock = 10L;
            long decreaseStock = 5L;
            Product product = ProductFixture.builder().withStock(initialStock).build();

            product.decreaseStock(decreaseStock);

            assertThat(product.getStock().count()).isEqualTo(initialStock - decreaseStock);
        }
    }
}
