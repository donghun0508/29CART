package com.loopers.interfaces.api.user;

import static java.util.Objects.requireNonNull;

import com.loopers.application.user.UserCommand.UserRegisterCommand;
import com.loopers.application.user.UserResult.UserDetailResult;
import com.loopers.domain.user.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;

public class UserV1Dto {

    @Builder
    public record SignUpRequest(
        String userId,
        String email,
        String birth,
        @NotNull GenderRequest gender
    ) {

        public UserRegisterCommand criteria() {
            return UserRegisterCommand.builder()
                .accountId(userId)
                .email(email)
                .birth(birth)
                .build();
        }

        public enum GenderRequest {
            M, F;

            public Gender toDomain() {
                return Gender.valueOf(this.name());
            }
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record UserResponse(
        String userId,
        String email,
        String birth,
        GenderResponse gender
    ) {

        public static UserResponse from(UserDetailResult userDetailResult) {
            requireNonNull(userDetailResult, "MemberResponse 객체가 null입니다.");

            return UserResponse.builder()
                .userId(userDetailResult.accountId())
                .email(userDetailResult.email())
                .birth(userDetailResult.birth())
                .gender(GenderResponse.toResponse(userDetailResult.gender()))
                .build();
        }

        public enum GenderResponse {
            M, F;

            public static GenderResponse toResponse(Gender gender) {
                return GenderResponse.valueOf(gender.name());
            }
        }
    }
}
