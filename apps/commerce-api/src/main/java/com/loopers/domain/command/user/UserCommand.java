package com.loopers.domain.command.user;

import com.loopers.domain.command.user.User.Gender;
import lombok.Builder;

public class UserCommand {

    @Builder
    public record Create(String userId, String email, String birth, Gender gender) {

    }

}
