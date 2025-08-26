package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.ChargeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "포인트 관리 API 입니다.")
public interface PointV1ApiSpec {

    @Operation(
        summary = "포인트 조회",
        description = "헤더의 회원 ID로 포인트 정보를 조회합니다."
    )
    ApiResponse<PointV1Dto.PointResponse> getPoint(
        @Parameter(hidden = true)
        String userId
    );

    @Operation(
        summary = "포인트 충전",
        description = "헤더의 회원 ID로 포인트를 충전합니다."
    )
    ApiResponse<PointV1Dto.PointResponse> chargePoint(
        @Parameter(hidden = true)
        String userId,
        @Schema(name = "포인트 충전 요청", description = "포인트")
        ChargeRequest request
    );
}
