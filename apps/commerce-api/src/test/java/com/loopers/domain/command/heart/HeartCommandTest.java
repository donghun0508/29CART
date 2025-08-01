package com.loopers.domain.command.heart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.environment.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@UnitTest
class HeartCommandTest {

    @DisplayName("좋아요 요청 객체 생성 시, ")
    @Nested
    class Create {

        @DisplayName("좋아요 타입이 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenHeartTypeIsNull(Long invalidUserId) {
            assertThatThrownBy(() -> HeartCommand.Create.builder().userId(invalidUserId).build())
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 타겟 ID가 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenTargetIdIsNull(Target invalidTarget) {
            assertThatThrownBy(() -> HeartCommand.Create.builder().userId(1L).target(invalidTarget).build())
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 객체를 생성한다.")
        @Test
        void createHeartCommand_whenValidParameters() {
            Long userId = 1L;
            Target target = Target.of(1L, TargetType.PRODUCT);

            HeartCommand.Create command = HeartCommand.Create.builder()
                .userId(userId)
                .target(target)
                .build();

            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.target()).isEqualTo(target);
        }
    }
}
