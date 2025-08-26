package com.loopers.domain.catalog;

import com.loopers.config.annotations.UnitTest;
import com.loopers.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class ProductTest {

    @DisplayName("재고 감소 시, 재고 파라미터가 null이면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenStockIsNull(Stock invalidStock) {
        Product product = ProductFixture.builder().build();

        assertThatThrownBy(() -> product.decreaseStock(invalidStock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product.decreaseStock().stock");
    }

    @DisplayName("재고 감소 시, 재고값이 0이하인 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void throwsException_whenStockValueIsZeroOrNegative(Long invalidStockValue) {
        Product product = ProductFixture.builder().build();
        Stock invalidStock = Stock.of(invalidStockValue);

        assertThatThrownBy(() -> product.decreaseStock(invalidStock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product.decreaseStock().stock.value");
    }

}
