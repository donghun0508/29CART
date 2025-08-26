package com.loopers.application.user;

import com.loopers.application.user.UserCommand.UserChargePointCommand;
import com.loopers.application.user.UserCommand.UserRegisterCommand;
import com.loopers.application.user.UserResult.UserDetailResult;
import com.loopers.application.user.UserResult.UserPointResult;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserFacade {

    private final UserService userService;

    public UserDetailResult signUp(UserRegisterCommand command) {
        User createUser = User.of(
            command.accountId(),
            command.email(),
            command.birth(),
            command.gender()
        );
        User savedUser = userService.create(createUser);
        return UserDetailResult.from(savedUser);
    }

    @Transactional
    public UserPointResult chargePoint(UserChargePointCommand command) {
        User user = userService.findByAccountId(command.accountId());
        user.chargePoint(command.amount());
        return UserPointResult.from(user);
    }

    public UserDetailResult getUser(AccountId accountId) {
        User user = userService.findByAccountId(accountId);
        return UserDetailResult.from(user);
    }

    public UserPointResult getPoint(AccountId accountId) {
        User user = userService.findByAccountId(accountId);
        return UserPointResult.from(user);
    }
}
