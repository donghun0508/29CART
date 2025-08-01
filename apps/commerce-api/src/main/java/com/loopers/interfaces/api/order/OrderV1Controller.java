package com.loopers.interfaces.api.order;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

import com.loopers.application.order.OrderFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderFacade orderFacade;

    @Override
    @PostMapping
    public ApiResponse<?> requestOrder(
        @RequestBody OrderV1Dto.RequestOrder.Request request,
        @RequestHeader(value = USER_ID, required = true) String userId
    ) {
        orderFacade.requestOrder(userId, request.toOrderForm());
        return ApiResponse.success();
    }
}
