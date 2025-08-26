package com.loopers.infrastructure.client;

import com.loopers.config.PaymentConfig;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "pgClient",
    url = "${external.payment.providers.simulator.base-url}",
    configuration = PaymentConfig.class
)
public interface PgClient {

    @GetMapping("/api/v1/payments")
    ApiResponse<OrderResponse> getTransactionsByOrder(@RequestParam("orderId") String orderId);

    @GetMapping("/api/v1/payments/{transactionKey}")
    ApiResponse<TransactionDetailResponse> getTransaction(@PathVariable String transactionKey);

    @PostMapping("/api/v1/payments")
    ApiResponse<PaymentResponse> requestPayment(@RequestBody PaymentRequest request);

}
