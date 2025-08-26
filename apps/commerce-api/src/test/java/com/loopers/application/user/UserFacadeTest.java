package com.loopers.application.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.application.user.UserCommand.UserChargePointCommand;
import com.loopers.config.annotations.IntegrationTest;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.AccountId;
import com.loopers.fixture.UserRegisterCommandFixture;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@IntegrationTest
class UserFacadeTest {

    @Autowired
    private UserFacade userFacade;

    @DisplayName("회원 가입 시, 동일한 회원 ID가 존재하면 예외를 발생한다.")
    @Test
    void signUpThrowsExceptionWhenDuplicateAccountId() {
        var command = UserRegisterCommandFixture.builder().build();
        userFacade.signUp(command);
        var duplicateCommand = UserRegisterCommandFixture.builder().accountId(command.accountId()).build();

        assertThatThrownBy(() -> userFacade.signUp(duplicateCommand))
            .isInstanceOf(CoreException.class)
            .asInstanceOf(InstanceOfAssertFactories.type(CoreException.class))
            .satisfies(ex -> {
                assertThat(ex.getErrorType()).isEqualTo(ErrorType.CONFLICT);
                assertThat(ex.getCause()).isInstanceOf(DataIntegrityViolationException.class);
            });
    }

    @DisplayName("회원 가입 시, 동일한 이메일이 존재하면 예외를 발생한다.")
    @Test
    void signUpThrowsExceptionWhenDuplicateEmail() {
        var command = UserRegisterCommandFixture.builder().build();
        userFacade.signUp(command);
        var duplicateCommand = UserRegisterCommandFixture.builder().email(command.email()).build();

        assertThatThrownBy(() -> userFacade.signUp(duplicateCommand))
            .isInstanceOf(CoreException.class)
            .asInstanceOf(InstanceOfAssertFactories.type(CoreException.class))
            .satisfies(ex -> {
                assertThat(ex.getErrorType()).isEqualTo(ErrorType.CONFLICT);
                assertThat(ex.getCause()).isInstanceOf(DataIntegrityViolationException.class);
            });
    }

    @DisplayName("회원 가입 시, 유효한 회원 생성 명령을 전달하면 회원 도메인을 생성한다.")
    @Test
    void signUpCreatesMember() {
        var command = UserRegisterCommandFixture.builder().build();

        var member = userFacade.signUp(command);

        assertThat(member).isNotNull();
        assertThat(member.accountId()).isEqualTo(command.accountId());
        assertThat(member.email()).isEqualTo(command.email());
        assertThat(member.birth()).isEqualTo(command.birth());
        assertThat(member.gender()).isEqualTo(command.gender());
    }

    @DisplayName("포인트 충전 시, 유효한 계정 ID와 금액을 전달하면 포인트를 충전한다.")
    @Test
    void chargePoint() {
        Long chargeAmount = 1000L;
        var userRegisterCriteria = UserRegisterCommandFixture.builder().build();
        var member = userFacade.signUp(userRegisterCriteria);

        var response = userFacade.chargePoint(
            new UserChargePointCommand(AccountId.of(member.accountId()), Money.of(chargeAmount))
        );

        assertThat(response).isNotNull();
        assertThat(response.accountId()).isEqualTo(member.accountId());
        assertThat(response.point()).isEqualTo(chargeAmount);
    }

}
