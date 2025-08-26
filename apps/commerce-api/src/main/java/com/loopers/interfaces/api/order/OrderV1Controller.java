package com.loopers.interfaces.api.order;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.order.OrderCommand.OrderRequestCommand;
import com.loopers.application.order.OrderPaymentFacade;
import com.loopers.domain.order.IdempotencyKey;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderPaymentFacade orderPaymentFacade;

    @Override
    @PostMapping
    public ApiResponse<?> requestOrderByPoint(
        @RequestBody OrderRequest request,
        @RequestHeader(value = USER_ID, required = true) String userId
    ) {
        OrderRequestCommand command = OrderRequestCommand.of(userId, request.idempotencyKey(), request.couponId(),
            request.toPurchaseMap(), request.paymentSpec());
        orderPaymentFacade.order(command);
        return ApiResponse.success();
    }

    @Override
    @GetMapping("/idempotency")
    public ApiResponse<String> requestOrderIdempotency() {
        return ApiResponse.success(IdempotencyKey.generate().key());
    }
}
