package com.loopers.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserCommand;
import com.loopers.domain.command.user.UserService;
import com.loopers.domain.command.fixture.UserCommandFixture;
import com.loopers.environment.annotations.IntegrationTest;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.user.UserAlreadyExistsException;
import com.loopers.support.error.user.UserNotFoundException;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import com.loopers.utils.DatabaseCleanUp;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
class UserFacadeIntegrationTest {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @MockitoSpyBean
    private UserService userService;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입 시, ")
    @Nested
    class SignUp {

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void throwsException_whenUserIdDuplicate() {
            // arrange
            String duplicateUserId = "testUser";
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::userId), duplicateUserId)
                .create();
            userFacade.signUp(command);

            // act
            UserAlreadyExistsException exception =
                assertThrows(UserAlreadyExistsException.class, () -> userFacade.signUp(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.CONFLICT);
        }

        @DisplayName("정상적인 회원 데이터인 경우, 회원가입에 성공한다.")
        @Test
        void doesNotThrow_whenValidUserData() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();

            // act
            UserInfo userInfo = userFacade.signUp(command);

            // assert
            assertThat(userInfo).isNotNull();
            assertThat(userInfo.userId()).isEqualTo(command.userId());
            assertThat(userInfo.gender()).isEqualTo(command.gender());
            assertThat(userInfo.birth()).isEqualTo(command.birth());
            assertThat(userInfo.email()).isEqualTo(command.email());
        }
    }

    @DisplayName("회원 조회 시, ")
    @Nested
    class Get {

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsUserInfo_whenValidIdIsProvided() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            UserInfo savedUserInfo = userFacade.signUp(command);

            // act
            UserInfo findUserInfo = userFacade.getUser(savedUserInfo.userId());

            // assert
            assertThat(findUserInfo).isNotNull();
            assertThat(findUserInfo.userId()).isEqualTo(savedUserInfo.userId());
            assertThat(findUserInfo.birth()).isEqualTo(savedUserInfo.birth());
            assertThat(findUserInfo.gender()).isEqualTo(savedUserInfo.gender());
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, UserNotFoundException 예외를 던진다.")
        @Test
        void throwsUserNotFoundException_whenGetUserWithNonExistentId() {
            // arrange
            String randomUserId = Instancio.create(String.class);

            // act
            UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> userFacade.getUser(randomUserId));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnsUserPoint_whenValidIdIsProvided() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            UserInfo savedUserInfo = userFacade.signUp(command);

            // act
            UserPointInfo findUserInfo = userFacade.getUserPoint(savedUserInfo.userId());

            // assert
            assertThat(findUserInfo).isNotNull();
            assertThat(findUserInfo.point()).isZero();
        }
    }

    @DisplayName("포인트 충전 시, ")
    @Nested
    class PointCharge {

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, UserNotFoundException 예외를 반환된다.")
        @Test
        void throwsUserNotFoundException_whenChargePointWithNonExistentId() {
            // arrange
            String randomId = Instancio.create(String.class);
            Long amount = 100L;

            // act
            UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> userFacade.chargePoint(randomId, amount));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 총 보유 포인트가 반환된다.")
        @Test
        void returnUserPointInfo_whenValidIdIsProvided() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            User savedUser = userService.create(command);
            Long chargeAmount = 1000L;

            // act
            UserPointInfo userPointInfo = userFacade.chargePoint(savedUser.getUserId(), chargeAmount);

            // assert
            assertThat(userPointInfo).isNotNull();
            assertThat(userPointInfo.point()).isEqualTo(chargeAmount);
        }
    }
}
