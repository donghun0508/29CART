package com.loopers.interfaces.api.point;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserPointInfo;
import com.loopers.interfaces.api.ApiHeaders;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    private final UserFacade userFacade;

    @GetMapping
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getPoint(
        @RequestHeader(value = USER_ID) String userId
    ) {
        UserPointInfo userPointInfo = userFacade.getUserPoint(userId);
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointInfo));
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> chargePoint(
        @RequestHeader(value = USER_ID) String userId,
        @RequestBody PointV1Dto.ChargeRequest request
    ) {
        UserPointInfo userPointInfo = userFacade.chargePoint(userId, request.amount());
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointInfo));
    }
}
