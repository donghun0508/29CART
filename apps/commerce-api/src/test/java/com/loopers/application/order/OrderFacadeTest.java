package com.loopers.application.order;

import com.loopers.application.order.CriteriaCommand.PointOrderCriteria;
import com.loopers.config.annotations.IntegrationTest;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@IntegrationTest
@Sql(scripts = "/data/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderFacadeTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private IssuedCouponService issuedCouponService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("재고가 존재하지 않은 경우, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenStockIsEmpty() {
        Map<Long, Long> purchaseProducts = Map.of(Long.MAX_VALUE, 1L);
        PointOrderCriteria criteria = PointOrderCriteria.of("test", null, purchaseProducts);

        assertThatExceptionOfType(CoreException.class)
                .isThrownBy(() -> orderFacade.orderByPoint(criteria))
                .satisfies(exception -> {
                    assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                })
                .withMessageContaining("ProductService.findAllWithLock()");
    }

    @DisplayName("재고가 부족할 경우, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenStockIsInsufficient() {
        Map<Long, Long> purchaseProducts = Map.of(1L, Long.MAX_VALUE);
        PointOrderCriteria criteria = PointOrderCriteria.of("test", null, purchaseProducts);

        assertThatThrownBy(() -> orderFacade.orderByPoint(criteria))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock.subtract().other");
    }

    @DisplayName("존재하지 않은 쿠폰 사용 시, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenCouponDoesNotExist() {
        Map<Long, Long> purchaseProducts = Map.of(1L, 1L);
        PointOrderCriteria criteria = PointOrderCriteria.of("test", Long.MAX_VALUE, purchaseProducts);

        assertThatExceptionOfType(CoreException.class)
                .isThrownBy(() -> orderFacade.orderByPoint(criteria))
                .satisfies(exception -> {
                    assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                })
                .withMessageContaining("IssuedCouponService.findByIdWithLock()");
    }

    @DisplayName("쿠폰 소유자와 일치하지 않는 쿠폰 사용 시, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenCouponOwnerDoesNotMatch() {
        Map<Long, Long> purchaseProducts = Map.of(1L, 1L);
        PointOrderCriteria criteria = PointOrderCriteria.of("test2", 1L, purchaseProducts);

        assertThatThrownBy(() -> orderFacade.orderByPoint(criteria))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Issuance.validate().targetId");
    }

    @DisplayName("주문 시 회원의 포인트 잔액이 부족할 경우, 예외가 발생한다.")
    @Test
    void throwsExceptionWhenUserPointIsInsufficient() {
        Map<Long, Long> purchaseProducts = Map.of(1L, 1L);
        PointOrderCriteria criteria = PointOrderCriteria.of("noPoint", null, purchaseProducts);

        assertThatThrownBy(() -> orderFacade.orderByPoint(criteria))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Money.subtract().other");
    }

    @DisplayName("동일한 쿠폰으로 여러 기기에서 동시에 주문해도, 쿠폰은 단 한번만 사용되어야 한다.")
    @Test
    void couponShouldBeUsedOnlyOnceEvenWhenOrderedSimultaneously() throws InterruptedException {
        int threadCount = 300;
        final Long couponId = 1L;
        final String accountId = "test";
        Map<Long, Long> purchaseProducts = Map.of(1L, 1L);
        PointOrderCriteria criteria = PointOrderCriteria.of(accountId, couponId, purchaseProducts);

        User user = userService.findByAccountId(AccountId.of(accountId));
        Money totalAmount = user.getTotalPoint();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        orderFacade.orderByPoint(criteria);
                        successCount.incrementAndGet();
                        log.info("주문 성공 : {}", criteria);
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        assertThat(e.getMessage()).contains("CouponState.used().status");
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(299);

        Order order = orderService.findById(1L);
        User buyer = userService.findByAccountId(AccountId.of(accountId));
        assertThat(order.getBuyerId()).isEqualTo(buyer.getId());
        assertThat(buyer.getTotalPoint()).isEqualTo(totalAmount.subtract(order.paidAmount()));

        IssuedCoupon issuedCoupon = issuedCouponService.findById(couponId);
        assertThat(issuedCoupon.isUsed()).isTrue();
        assertThat(issuedCoupon.getTargetId()).isEqualTo(buyer.getId());
    }

    @DisplayName("동일한 유저가 서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다.")
    @Test
    void userPointShouldBeDeductedCorrectlyWhenMultipleOrdersArePlacedSimultaneously() throws InterruptedException {
        String accountId = "test";
        User user = userService.findByAccountId(AccountId.of(accountId));
        Money totalAmount = user.getTotalPoint();
        Random random = new Random(System.currentTimeMillis());

        // 스레드 0번과 스레드 3번이 중복되는 상품이 없어서 비관적락을 사용해도 동시성 문제가 발생한다.
        Map<Integer, Map<Long, Long>> threadOrders = new HashMap<>();
        threadOrders.put(0, Map.of(1L, (long) random.nextInt(5) + 1, 2L, (long) random.nextInt(5) + 1, 3L, (long) random.nextInt(5) + 1)); // 1,2,3 상품
        threadOrders.put(1, Map.of(2L, (long) random.nextInt(5) + 1, 3L, (long) random.nextInt(5) + 1, 4L, (long) random.nextInt(5) + 1)); // 2,3,4 상품
        threadOrders.put(2, Map.of(3L, (long) random.nextInt(5) + 1, 4L, (long) random.nextInt(5) + 1, 5L, (long) random.nextInt(5) + 1)); // 3,4,5 상품
        threadOrders.put(3, Map.of(4L, (long) random.nextInt(5) + 1, 5L, (long) random.nextInt(5) + 1, 6L, (long) random.nextInt(5) + 1)); // 4,5,6 상품
        threadOrders.put(4, Map.of(5L, (long) random.nextInt(5) + 1, 6L, (long) random.nextInt(5) + 1, 7L, (long) random.nextInt(5) + 1)); // 5,6,7 상품

        List<Product> initialProducts = productService.findAll(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        Map<Long, Long> initialStocks = initialProducts.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        p -> p.getStock().count()
                ));

        Map<Long, Product> productMap = initialProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<Integer, Money> threadTotalAmounts = threadOrders.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Map<Long, Long> orderProducts = entry.getValue();
                            return orderProducts.entrySet().stream()
                                    .map(productEntry -> {
                                        Long productId = productEntry.getKey();
                                        Long quantity = productEntry.getValue();
                                        Product product = productMap.get(productId);
                                        return product.getUnitPrice().multiply(quantity);
                                    })
                                    .reduce(Money.ZERO, Money::add);
                        }
                ));

        Money productTotalAmount = threadTotalAmounts.values().stream()
                .reduce(Money.ZERO, Money::add);

        int threadCount = threadOrders.size();
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                final int threadIndex = i;
                executorService.submit(() -> {
                    try {
                        Map<Long, Long> products = threadOrders.get(threadIndex);
                        PointOrderCriteria criteria = PointOrderCriteria.of(accountId, null, products);
                        orderFacade.orderByPoint(criteria);
                        successCount.incrementAndGet();
                        log.info("주문 성공 - 스레드 {}: {}", threadIndex, criteria);
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        log.error("주문 실패 - 스레드 {}: {}", threadIndex, e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        assertThat(successCount.get()).isEqualTo(5);
        assertThat(failCount.get()).isEqualTo(0);

        List<Product> finalProducts = productService.findAll(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        Map<Long, Long> finalStocks = finalProducts.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        p -> p.getStock().count()
                ));

        Map<Long, Long> expectedDeductions = new HashMap<>();
        for (Map<Long, Long> orderProducts : threadOrders.values()) {
            for (Map.Entry<Long, Long> entry : orderProducts.entrySet()) {
                Long productId = entry.getKey();
                Long quantity = entry.getValue();
                expectedDeductions.merge(productId, quantity, Long::sum);
            }
        }

        expectedDeductions.forEach((productId, expectedDeduction) -> {
            Long expectedFinalStock = initialStocks.get(productId) - expectedDeduction;
            assertThat(finalStocks.get(productId))
                    .as("상품 %d의 재고가 예상과 다릅니다", productId)
                    .isEqualTo(expectedFinalStock);
        });

        User finalUser = userService.findByAccountId(AccountId.of(accountId));
        Money expectedFinalPoint = totalAmount.subtract(productTotalAmount);
        assertThat(finalUser.getTotalPoint()).isEqualTo(expectedFinalPoint);
    }

    @DisplayName("동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다.")
    @Test
    void stockShouldBeDeductedCorrectlyWhenMultipleOrdersForSameProductArePlacedSimultaneously() throws InterruptedException {
        Map<Integer, Map<Long, Long>> threadOrders = new HashMap<>();
        Random random = new Random(System.currentTimeMillis());
        final Long defaultProductId = 1L;

        List<User> users = userService.findAllByIdIn(List.of(1L, 2L, 3L));
        for (int i = 0; i < users.size(); i++) {
            threadOrders.put(i, Map.of(defaultProductId, (long) random.nextInt(5) + 1));
        }

        Long totalQuantity = threadOrders.values().stream()
                .flatMap(orderMap -> orderMap.values().stream())
                .mapToLong(Long::longValue)
                .sum();

        Product product = productService.findById(defaultProductId);
        Long initialStock = product.getStock().count();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(threadOrders.size());

        try (ExecutorService executorService = Executors.newFixedThreadPool(users.size())) {
            for (int i = 0; i < threadOrders.size(); i++) {
                final int threadIndex = i;
                executorService.submit(() -> {
                    try {
                        Map<Long, Long> products = threadOrders.get(threadIndex);
                        PointOrderCriteria criteria = PointOrderCriteria.of(users.get(threadIndex).getAccountId().value(), null, products);
                        orderFacade.orderByPoint(criteria);
                        successCount.incrementAndGet();
                        log.info("주문 성공 - 스레드 {}: {}", threadIndex, criteria);
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        log.error("주문 실패 - 스레드 {}: {}", threadIndex, e.getMessage());
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }

        boolean completed = latch.await(30, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        Product finalProduct = productService.findById(defaultProductId);
        assertThat(finalProduct.getStock().count()).isEqualTo(initialStock - totalQuantity);
    }
}
