package com.loopers.interfaces.api.user;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.command.user.UserCommand;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.SignUpRequest;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserCommand.Create command = request.toCommand();
        UserInfo userInfo = userFacade.signUp(command);
        return ApiResponse.success(UserResponse.from(userInfo));
    }

    @GetMapping("/me")
    @Override
    public ApiResponse<UserResponse> getUser(
        @RequestHeader(value = USER_ID) String userId
    ) {
        UserInfo userInfo = userFacade.getUser(userId);
        return ApiResponse.success(UserResponse.from(userInfo));
    }
}
