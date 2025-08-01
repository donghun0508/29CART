package com.loopers.application.user;

import com.loopers.domain.command.user.User;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserPointInfo(
    Long point,
    String userId
) {

    public static UserPointInfo from(User user) {
        Objects.requireNonNull(user, "User 객체가 null입니다.");

        return UserPointInfo.builder()
            .point(user.getAvailablePoints().value())
            .userId(user.getUserId())
            .build();
    }
}
