package com.loopers.application.heart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.domain.command.fixture.ProductFixture;
import com.loopers.domain.command.fixture.UserFixture;
import com.loopers.domain.command.heart.Heart;
import com.loopers.domain.command.heart.HeartService;
import com.loopers.domain.command.heart.Target;
import com.loopers.domain.command.heart.TargetType;
import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.product.ProductRepository;
import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserRepository;
import com.loopers.environment.annotations.IntegrationTest;
import jakarta.persistence.Table;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@IntegrationTest
class HeartFacadeIntegrationTest {

    @Autowired
    private HeartFacade heartFacade;

    @Autowired
    private HeartService heartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("좋아요 등록 시, ")
    @Nested
    class Like {

        @DisplayName("복합 유니크 키 설정 없이 동시 좋아요 테스트")
        @Test
        @DisabledIf("hasUniqueConstraint")
        void concurrentLikeTest_withUniqueConstraint() throws InterruptedException {
            executeConcurrentLikeTest(
                heartFacade::likeUnsafe,
                count -> assertThat(count).isEqualTo(1)
            );
        }

        @DisplayName("복합 유니크 키 설정 시 동시 좋아요 테스트")
        @Test
        @EnabledIf("hasUniqueConstraint")
        void concurrentLikeUnsafeTest_withoutUniqueConstraint() throws InterruptedException {
            executeConcurrentLikeTest(
                heartFacade::like,
                count -> assertThat(count).isEqualTo(1)
            );
        }

        private void executeConcurrentLikeTest(BiConsumer<String, Target> likeAction, Consumer<Integer> assertion) throws InterruptedException {
            int concurrentRequests = 300;
            CountDownLatch latch = new CountDownLatch(concurrentRequests);

            User user = userRepository.save(UserFixture.integration().build());
            Product product = productRepository.save(ProductFixture.integration().build());
            Target target = Target.of(product.getId(), TargetType.PRODUCT);

            try (ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests)) {
                IntStream.range(0, concurrentRequests)
                    .forEach(i -> executor.submit(() -> {
                        try {
                            likeAction.accept(user.getUserId(), target);
                        } catch (Exception e) {
                            log.error("좋아요 요청 중 오류 발생: {}", e.getMessage());
                        } finally {
                            latch.countDown();
                        }
                    }));

                latch.await();
            }

            List<Heart> actualHearts = heartService.findAllByUserIdAndTargetType(user.getId(), TargetType.PRODUCT);
            assertion.accept(actualHearts.size());
            log.info("요청 {}번 → 실제 저장 {}개", concurrentRequests, actualHearts.size());
        }

        boolean hasUniqueConstraint() {
            return Heart.class.getAnnotation(Table.class).uniqueConstraints().length > 0;
        }
    }

    @DisplayName("좋아요 취소 시, ")
    @Nested
    class UnLike {

        @DisplayName("회원 데이터만 존재하고 좋아요가 없는 경우 정상 처리된다.")
        @Test
        void unlike_whenNoHeartExists() {
            User user = userRepository.save(UserFixture.integration().build());
            Product product = productRepository.save(ProductFixture.integration().build());
            Target target = Target.of(product.getId(), TargetType.PRODUCT);

            heartFacade.unlike(user.getUserId(), target);

            List<Heart> actualHearts = heartService.findAllByUserIdAndTargetType(user.getId(), TargetType.PRODUCT);
            assertThat(actualHearts.size()).isEqualTo(0);
        }

        @DisplayName("Heart 데이터를 삭제한다.")
        @Test
        void unlike() {
            User user = userRepository.save(UserFixture.integration().build());
            Product product = productRepository.save(ProductFixture.integration().build());
            Target target = Target.of(product.getId(), TargetType.PRODUCT);
            heartFacade.like(user.getUserId(), target);

            heartFacade.unlike(user.getUserId(), target);

            List<Heart> actualHearts = heartService.findAllByUserIdAndTargetType(user.getId(), TargetType.PRODUCT);
            assertThat(actualHearts.size()).isEqualTo(0);
        }

    }
}
