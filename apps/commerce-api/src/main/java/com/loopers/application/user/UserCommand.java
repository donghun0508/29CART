package com.loopers.application.user;

import com.loopers.domain.shared.Money;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.Gender;
import lombok.Builder;

public class UserCommand {

    @Builder
    public record UserRegisterCommand(
        String accountId,
        String email,
        String birth,
        Gender gender
    ) {

    }

    public record UserChargePointCommand(
        AccountId accountId,
        Money amount
    ) {

    }
}
