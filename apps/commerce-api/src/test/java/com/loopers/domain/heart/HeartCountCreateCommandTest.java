package com.loopers.domain.heart;

import com.loopers.application.heart.HeartCommand.LikeCommand;
import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.user.AccountId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class HeartCountCreateCommandTest {

    @DisplayName("좋아요 생성 시, 사용자 ID가 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenUserIdIsNull() {
        assertThatThrownBy(() -> new LikeCommand(AccountId.of(null), Target.of(1L, TargetType.PRODUCT)))
                        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("좋아요 생성 시, 대상 ID가 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenTargetIdIsNull() {
        assertThatThrownBy(() -> new LikeCommand(AccountId.of("test"), Target.of(null, TargetType.PRODUCT)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("좋아요 생성 시, 대상 타입이 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenTargetTypeIsNull() {
        assertThatThrownBy(() -> new LikeCommand(AccountId.of("test"), Target.of(1L, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
