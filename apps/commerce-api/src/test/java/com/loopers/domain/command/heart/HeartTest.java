package com.loopers.domain.command.heart;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.domain.command.fixture.HeartCommandFixture;
import com.loopers.environment.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@UnitTest
public class HeartTest {

    @DisplayName("좋아요 객체 생성 시, ")
    @Nested
    class Create {

        @DisplayName("좋아요 명령 객체가 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenHeartCommandIsNull(HeartCommand.Create invalidCommand) {
            assertThatThrownBy(() -> Heart.create(invalidCommand))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 명령 객체가 유효한 경우 좋아요 객체를 생성한다.")
        @Test
        void createHeartCommand_whenValidParameters() {
            HeartCommand.Create command = HeartCommandFixture.Create.builder().build();

            Heart heart = Heart.create(command);

            assertThat(heart).satisfies(h -> {
                assertThat(h.getUserId()).isEqualTo(command.userId());
                assertThat(h.getTarget()).isEqualTo(command.target());
            });
        }

    }

}
