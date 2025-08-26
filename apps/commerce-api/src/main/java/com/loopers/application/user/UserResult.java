package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public class UserResult {

    public record UserDetailResult(String accountId, String email, String birth, Gender gender) {

        public static UserDetailResult from(User user) {
            return new UserDetailResult(
                user.getAccountId().value(),
                user.getEmail().address(),
                user.getBirth().day(),
                user.getGender()
            );
        }
    }

    public record UserPointResult(String accountId, Long point) {

        public static UserPointResult from(User user) {
            return new UserPointResult(user.getAccountId().value(), user.getTotalPoint().value());
        }
    }
}
