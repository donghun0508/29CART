package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "회원 관리 API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "새로운 회원을 등록합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> signUp(
        @Schema(name = "회원 가입 요청", description = "가입할 회원의 정보")
        UserV1Dto.SignUpRequest request
    );

    @Operation(
        summary = "회원 조회",
        description = "ID로 회원 정보를 조회합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> getUser(
        @Parameter(hidden = true)
        String userId
    );
}
