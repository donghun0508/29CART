package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public interface OrderV1ApiSpec {

    @Operation(
        summary = "주문 요청",
        description = "사용자가 주문 요청 및 포인트 결제를 합니다."
    )
    ApiResponse<?> requestOrderByPoint(
        @Parameter(description = "주문 요청 내용") OrderRequest request,
        @Parameter(hidden = true) String userId
    );

    @Operation(
        summary = "주문 멱등키 요청",
        description = "주문 요청 시 멱등성을 보장하기 위한 키를 생성"
    )
    ApiResponse<String> requestOrderIdempotency();
}
