package com.loopers.domain.heart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.config.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class HeartCountTest {

    @DisplayName("좋아요 도메인 생성 시, ")
    @Nested
    class Create {

        @DisplayName("유효한 좋아요 생성 명령을 전달하면 좋아요 도메인을 생성한다.")
        @Test
        void createHeart() {
            var userId = 1L;
            var target = Target.of(1L, TargetType.PRODUCT);

            var heart = Heart.from(userId, target);

            assertThat(heart).isNotNull();
            assertThat(heart.getUserId()).isEqualTo(userId);
            assertThat(heart.getTarget()).isEqualTo(target);
        }
    }

}
