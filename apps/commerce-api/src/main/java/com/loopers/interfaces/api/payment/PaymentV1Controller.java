package com.loopers.interfaces.api.payment;

import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.payment.PaymentV1Dto.TransactionCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentV1Controller implements PaymentV1ApiSpec {

    private final PaymentFacade paymentFacade;

    @Override
    @PostMapping("/callback")
    public ApiResponse<?> callback(@RequestBody TransactionCallback transactionCallback) {
        PaymentSyncCommand command = transactionCallback.toCommand();
        paymentFacade.syncPayment(command);
        return ApiResponse.success();
    }
}
