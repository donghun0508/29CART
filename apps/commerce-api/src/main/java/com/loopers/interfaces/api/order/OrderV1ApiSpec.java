package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface OrderV1ApiSpec {

    @Operation(
        summary = "주문 요청",
        description = "사용자가 주문 요청 및 결제를 합니다."
    )
    ApiResponse<?> requestOrder(
        @Parameter(description = "주문 요청 내용") OrderV1Dto.RequestOrder.Request request,
        @Parameter(hidden = true) String userId
    );
}
