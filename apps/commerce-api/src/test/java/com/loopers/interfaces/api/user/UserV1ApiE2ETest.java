package com.loopers.interfaces.api.user;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.environment.annotations.E2ETest;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.fixture.UserV1DtoFixture;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@E2ETest
class UserV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/users";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2ETest(
        TestRestTemplate testRestTemplate,
        DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Post {

        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserResponse_whenValidUserData() {
            // arrange
            UserV1Dto.SignUpRequest request = UserV1DtoFixture.SignUpRequest.complete().create();

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().data()).isNotNull(),
                () -> {
                    var actual = response.getBody().data();
                    assertThat(actual.userId()).isEqualTo(request.userId());
                    assertThat(actual.email()).isEqualTo(request.email());
                    assertThat(actual.birth()).isEqualTo(request.birth());
                    assertThat(actual.gender().name()).isEqualTo(request.gender().name());
                }
            );
        }

        @DisplayName("회원 가입 시 성별이 없을 경우, 400 Bad Request를 응답한다.")
        @Test
        void returnsBadRequest_whenGenderIsMissing() {
            var request = UserV1DtoFixture.SignUpRequest.complete()
                .ignore(field(UserV1Dto.SignUpRequest::gender))
                .create();

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(() -> assertTrue(response.getStatusCode().is4xxClientError()));
        }
    }

    @DisplayName("GET /api/v1/users/{id}")
    @Nested
    class GET {

        @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returns404NotFound_whenUserDoesNotExist() {
            // arrange
            String userId = "12345";
            HttpHeaders headers = new HttpHeaders();
            headers.set(USER_ID, userId);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(ENDPOINT + "/me", HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError())
            );
        }

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserResponse_whenValidIdIsProvided() {
            // arrange
            UserV1Dto.SignUpRequest request = UserV1DtoFixture.SignUpRequest.complete().create();
            String userId = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                })
                .getBody()
                .data()
                .userId();

            HttpHeaders headers = new HttpHeaders();
            headers.set(USER_ID, String.valueOf(userId));

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(ENDPOINT + "/me", HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().data()).isNotNull(),
                () -> {
                    var actual = response.getBody().data();
                    assertThat(actual.userId()).isEqualTo(request.userId());
                    assertThat(actual.email()).isEqualTo(request.email());
                    assertThat(actual.birth()).isEqualTo(request.birth());
                    assertThat(actual.gender().name()).isEqualTo(request.gender().name());
                }
            );
        }
    }
}
