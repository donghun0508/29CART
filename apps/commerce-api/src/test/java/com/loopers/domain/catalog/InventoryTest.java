package com.loopers.domain.catalog;

import com.loopers.config.annotations.UnitTest;
import com.loopers.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class InventoryTest {

    @DisplayName("상품 관리자 할당 시, 상품 목록은 비어있을 수 없습니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwsExceptionWhenProductListIsNullOrEmpty(List<Product> invalidProductList) {
        Map<Long, Stock> stockMap = Map.of(1L, new Stock(1L));

        assertThatThrownBy(() -> Inventory.allocate(invalidProductList, stockMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ProductManager.assign().products");
    }

    @DisplayName("상품 관리자 할당 시, 재고 목록은 비어있을 수 없습니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwsExceptionWhenStockMapIsNullOrEmpty(Map<Long, Stock> invalidStockMap) {
        List<Product> products = List.of(
                ProductFixture.builder().build(),
                ProductFixture.builder().build(),
                ProductFixture.builder().build(),
                ProductFixture.builder().build(),
                ProductFixture.builder().build()
        );

        assertThatThrownBy(() -> Inventory.allocate(products, invalidStockMap))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ProductManager.assign().purchaseProducts");
    }

    @DisplayName("상품 관리자 할당 후 재고 차감 시, 상품 ID와 구매할 상품 ID가 일치하지 않는 경우 예외가 발생한다.")
    @Test
    void throwsExceptionWhenProductIdDoesNotMatch() {
        List<Product> products = List.of(
                ProductFixture.builder().id(1L).build(),
                ProductFixture.builder().id(2L).build()
        );
        Map<Long, Stock> stockMap = Map.of(
                1L, new Stock(1L),
                3L, new Stock(1L)
        );

        Inventory inventory = Inventory.allocate(products, stockMap);

        assertThatThrownBy(inventory::decreaseStock)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ProductManager.decreaseStock()");
    }

    @DisplayName("구매할 상품의 수량이 0인 경우 예외가 발생한다.")
    @Test
    void throwsExceptionWhenStockIsEmpty() {
        Stock productStock = Stock.of(10L);
        Stock buyStock = Stock.of(11L);

        List<Product> products = List.of(ProductFixture.builder().id(1L).stock(productStock).build());
        Map<Long, Stock> stockMap = Map.of(1L, buyStock);

        Inventory inventory = Inventory.allocate(products, stockMap);

        assertThatThrownBy(inventory::decreaseStock)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
