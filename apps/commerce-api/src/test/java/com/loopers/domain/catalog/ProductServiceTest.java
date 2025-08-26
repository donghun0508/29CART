package com.loopers.domain.catalog;

import com.loopers.config.annotations.IntegrationTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@IntegrationTest
@Sql(scripts = "/data/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 목록 조회 시 조회 결과와 조회 상품 개수와 일치하지 않으면 예외가 발생한다")
    @Test
    void throwsExceptionWhenProductCountDoesNotMatch() {
        List<Long> productIds = List.of(1L, 2L, 3L, Long.MAX_VALUE);

        assertThatExceptionOfType(CoreException.class)
                .isThrownBy(() -> productService.findAll(productIds))
                .satisfies(exception -> {
                    assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                });
    }

    @DisplayName("상품 목록 조회 시 조회 결과와 조회 상품 개수가 일치하면 정상적으로 조회된다")
    @Test
    void returnsProductsWhenProductCountMatches() {
        List<Long> productIds = List.of(1L, 2L, 3L);

        List<Product> products = productService.findAll(productIds);

        assertThat(products).hasSize(productIds.size())
                .extracting(Product::getId)
                .containsExactlyInAnyOrderElementsOf(productIds);
    }
}
