package com.loopers.domain.command.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserCommand;
import com.loopers.domain.command.user.UserRepository;
import com.loopers.domain.command.user.UserService;
import com.loopers.domain.command.fixture.UserCommandFixture;
import com.loopers.environment.annotations.IntegrationTest;
import com.loopers.support.error.user.UserNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserRepository userRepository;

    @DisplayName("회원 서비스에서,")
    @Nested
    class Create {

        @DisplayName("회원 도메인 객체 생성 후 User 저장이 수행된다. ( spy 검증 )")
        @Test
        void returnsUser_whenCreateUser() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            User expectedUser = User.of(command);
            doReturn(expectedUser).when(userRepository).save(any(User.class));

            // act
            User savedUser = userService.create(command);

            // assert
            assertUserEquals(savedUser, expectedUser);
        }
    }

    @DisplayName("회원 서비스에서, ")
    @Nested
    class Read {

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsUser_whenValidIdIsProvided() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            User savedUser = userService.create(command);

            // act
            User foundUser = userService.findByUserId(savedUser.getUserId());

            // assert
            assertUserEquals(foundUser, savedUser);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, 예외를 반환한다.")
        @Test
        void returnNull_whenInValidIdIsProvided() {
            String randomUserId = Instancio.create(String.class);

            assertThatThrownBy(() -> userService.findByUserId(randomUserId))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    private void assertUserEquals(User actual, User expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getBirth()).isEqualTo(expected.getBirth());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getGender()).isEqualTo(expected.getGender());
    }
}
