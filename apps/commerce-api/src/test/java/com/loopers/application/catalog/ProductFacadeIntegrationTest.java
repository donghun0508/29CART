package com.loopers.application.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.config.annotations.IntegrationTest;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;


@IntegrationTest
@Sql(scripts = "/data/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductFacadeIntegrationTest {

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("특정 상품에 동시 좋아요가 요청 될 때, 좋아요 수가 정확히 반영된다.")
    @Test
    void concurrentHeart() throws InterruptedException {
        final var threadCount = 10;
        final var productId = 1L;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        productFacade.incrementHeart(productId);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);

        Product product = productRepository.findById(productId).orElseThrow();
        assertThat(product.getHeartCount()).isEqualTo(threadCount);
    }

    @DisplayName("특정 상품에 좋아요 취소 시 좋아요 수가 0이하인 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenDecrementHeartBelowZero() {
        final var productId = 1L;

        assertThatThrownBy(() ->  productFacade.decrementHeart(productId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("HeartCount.down().value");
    }

    @DisplayName("특정 상품에 좋아요 수가 0인경우, 동시에 좋아요 취소가 요청되면, 좋아요 수가 음수가 되지 않는다.")
    @Test
    void concurrentDecrementHeartBelowZero() throws InterruptedException {
        final var threadCount = 10;
        final var productId = 1L;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        productFacade.decrementHeart(productId);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        latch.await(10, TimeUnit.SECONDS);


        Product product = productRepository.findById(productId).orElseThrow();
        assertThat(product.getHeartCount()).isEqualTo(0);
    }
}
