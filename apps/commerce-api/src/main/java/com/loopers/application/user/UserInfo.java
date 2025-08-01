package com.loopers.application.user;

import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.User.Gender;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfo(
    String userId,
    String email,
    String birth,
    Gender gender
) {

    public static UserInfo from(User user) {
        Objects.requireNonNull(user, "User 객체가 null입니다.");

        return UserInfo.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .birth(user.getBirth())
            .gender(user.getGender())
            .build();
    }
}
