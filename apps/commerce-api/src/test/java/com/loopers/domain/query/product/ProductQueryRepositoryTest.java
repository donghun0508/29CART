package com.loopers.domain.query.product;


import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.query.shared.PageConstants;
import com.loopers.environment.annotations.RepositoryTest;
import com.loopers.infrastructure.product.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@RepositoryTest
@Sql(scripts = {"classpath:data/test-data.sql",}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:data/test-cleanup.sql",}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductQueryRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @DisplayName("상품 목록 조회 시, ")
    @Nested
    class Get {

        @DisplayName("검색 조건이 없을 때, 기본 페이징에 맞춰 상품 목록을 조회한다.")
        @Test
        void findProducts() {
            var result = productJpaRepository.findProducts(PageConstants.defaultPageable());

            assertThat(result).isNotEmpty();
            assertThat(result.getTotalElements()).isGreaterThan(1L);
        }
    }
}
