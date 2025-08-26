package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.payment.PaymentV1Dto.TransactionCallback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment V1 API", description = "결제 관리 API 입니다.")
public interface PaymentV1ApiSpec {

    @Operation(
        summary = "결제 콜백 처리",
        description = "PG사로부터 전달받은 결제 완료 알림을 처리하고 결제 상태를 업데이트합니다."
    )
    ApiResponse<?> callback(TransactionCallback transactionCallback);
}
