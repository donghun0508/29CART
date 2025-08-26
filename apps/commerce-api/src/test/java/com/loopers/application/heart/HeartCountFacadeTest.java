package com.loopers.application.heart;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.application.heart.HeartCommand.LikeCommand;
import com.loopers.application.heart.HeartCommand.UnlikeCommand;
import com.loopers.application.user.UserCommand.UserRegisterCommand;
import com.loopers.config.annotations.IntegrationTest;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandRepository;
import com.loopers.domain.catalog.HeartCount;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductRepository;
import com.loopers.domain.heart.HeartRepository;
import com.loopers.domain.heart.Target;
import com.loopers.domain.heart.TargetType;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.fixture.BrandFixture;
import com.loopers.fixture.ProductFixture;
import com.loopers.fixture.UserRegisterCommandFixture;
import com.loopers.utils.DatabaseCleanUp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@IntegrationTest
@TestPropertySource(properties = {
//        "logging.level.root=OFF",
    "logging.level.org.hibernate.engine.jdbc.spi.SqlExceptionHelper=OFF",
    "logging.level.com.mysql.cj.jdbc.exceptions=OFF"
})
class HeartCountFacadeTest {

    @Autowired
    private HeartFacade heartFacade;

    @Autowired
    private HeartRepository heartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("동일한 상품에 여러명이 따닥(중복) 좋아요 요청을 보내면, 최종 좋아요 수는 사용자 수와 동일하다")
    @Test
    void concurrentDuplicateHeartTest() throws InterruptedException {
        int userCount = 10;
        int requestsPerUser = 5;
        int totalRequests = userCount * requestsPerUser;

        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            UserRegisterCommand command = UserRegisterCommandFixture.builder().email("Email" + i + "@gmail.com").build();
            User user = userRepository.save(User.of(command.accountId(), command.email(), command.birth(), command.gender()));
            users.add(user);
        }

        Brand brand = brandRepository.save(BrandFixture.persistence().build());

        Product product = productRepository.save(ProductFixture.persistence().brand(brand).like(
            HeartCount.zero()).build());
        Target target = Target.of(product.getId(), TargetType.PRODUCT);

        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newFixedThreadPool(userCount)) {
            for (int userIndex = 0; userIndex < userCount; userIndex++) {
                final User currentUser = users.get(userIndex);

                for (int requestIndex = 0; requestIndex < requestsPerUser; requestIndex++) {
                    executor.submit(() -> {
                        try {
                            LikeCommand likeCommand = LikeCommand.of(currentUser.getAccountId().value(), target.targetId(), target.targetType());
                            heartFacade.heart(likeCommand);
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failCount.incrementAndGet();
                            log.error("Error processing like request for user {}: {}", currentUser.getAccountId(), e.getMessage(), e);
                        } finally {
                            latch.countDown();
                        }
                    });
                }
            }
        }

        boolean completed = latch.await(60, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        log.info("총 요청 수: {}, 성공: {}, 실패: {}", totalRequests, successCount.get(), failCount.get());

        long actualLikeCount = heartRepository.count();
        assertThat(actualLikeCount).isEqualTo(userCount);

        Product actualProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(actualProduct.getHeartCount()).isEqualTo(userCount);
    }

    @DisplayName("여러명이 동일한 상품에 좋아요 취소 한 경우, 최종 좋아요 수는 0이 된다")
    @Test
    void concurrentUnHeartTest() throws InterruptedException {
        int userCount = 10;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            UserRegisterCommand command = UserRegisterCommandFixture.builder().email("Email" + i + "@gmail.com").build();
            User user = userRepository.save(User.of(command.accountId(), command.email(), command.birth(), command.gender()));
            users.add(user);
        }

        Brand brand = brandRepository.save(BrandFixture.persistence().build());
        Product product = productRepository.save(ProductFixture.persistence().brand(brand).like(
            HeartCount.zero()).build());
        Target target = Target.of(product.getId(), TargetType.PRODUCT);

        for (User user : users) {
            LikeCommand likeCommand = LikeCommand.of(user.getAccountId().value(), target.targetId(), target.targetType());
            heartFacade.heart(likeCommand);
        }

        assertThat(heartRepository.count()).isEqualTo(userCount);
        assertThat(productRepository.findById(product.getId()).orElseThrow().getHeartCount()).isEqualTo(userCount);

        int requestsPerUser = 5;
        int totalRequests = userCount * requestsPerUser;

        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newFixedThreadPool(userCount)) {
            for (int userIndex = 0; userIndex < userCount; userIndex++) {
                final User currentUser = users.get(userIndex);

                for (int requestIndex = 0; requestIndex < requestsPerUser; requestIndex++) {
                    executor.submit(() -> {
                        try {
                            UnlikeCommand unlikeCommand = UnlikeCommand.of(currentUser.getAccountId().value(), target.targetId(), target.targetType());
                            heartFacade.unHeart(unlikeCommand);
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failCount.incrementAndGet();
                            log.error("Error processing like request for user {}: {}", currentUser.getAccountId(), e.getMessage(), e);
                        } finally {
                            latch.countDown();
                        }
                    });
                }
            }
        }

        boolean completed = latch.await(60, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        log.info("총 요청 수: {}, 성공: {}, 실패: {}", totalRequests, successCount.get(), failCount.get());

        long actualLikeCount = heartRepository.count();
        assertThat(actualLikeCount).isZero();

        Product actualProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(actualProduct.getHeartCount()).isZero();
    }
}
