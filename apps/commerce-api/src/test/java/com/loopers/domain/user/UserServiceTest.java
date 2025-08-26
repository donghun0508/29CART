package com.loopers.domain.user;

import com.loopers.config.annotations.IntegrationTest;
import com.loopers.fixture.UserRegisterCommandFixture;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IntegrationTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @DisplayName("회원 생성 시, 유효한 회원 생성 명령을 전달하면 회원 도메인을 생성한다.")
    @Test
    void createMember() {
        var command = UserRegisterCommandFixture.builder().build();
        var user = User.of(command.accountId(), command.email(), command.birth(), command.gender());

        var savedUser = userService.create(user);

        assertThat(savedUser.getAccountId()).isNotNull();
        assertThat(savedUser.getId()).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("회원 조회 시, 존재하지 않는 회원 ID를 전달하면 예외를 발생시킨다.")
    @Test
    void findByAccountIdThrowsExceptionWhenNotFound() {
        var invalidAccountId = AccountId.of("test");

        assertThatThrownBy(() -> userService.findByAccountId(invalidAccountId))
                .asInstanceOf(InstanceOfAssertFactories.type(CoreException.class))
                .satisfies(ex -> {
                    assertThat(ex.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                });
    }

    @DisplayName("회원 조회 시, 유효한 회원 ID를 전달하면 회원 도메인을 반환한다.")
    @Test
    void findByAccountId() {
        var command = UserRegisterCommandFixture.builder().build();
        var user = User.of(command.accountId(), command.email(), command.birth(), command.gender());

        var savedUser = userService.create(user);

        var foundMember = userService.findByAccountId(savedUser.getAccountId());
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getId()).isEqualTo(savedUser.getId());
        assertThat(foundMember.getAccountId()).isEqualTo(savedUser.getAccountId());
    }
}
