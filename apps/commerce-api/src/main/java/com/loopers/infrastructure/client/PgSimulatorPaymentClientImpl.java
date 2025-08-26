package com.loopers.infrastructure.client;

import static com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig.Constants.CIRCUIT_PAYMENT;
import static com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig.Constants.RETRY_PAYMENT;

import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClientData.PaymentClientRequest;
import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentClientData.PaymentClientTransactionDetailResponse;
import com.loopers.domain.payment.SupportPg.PgSimulator;
import com.loopers.domain.payment.TransactionStatus;
import com.loopers.interfaces.api.ApiResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@PgSimulator
class PgSimulatorPaymentClientImpl implements PaymentClient {

    private final PgClient pgClient;
    private final PaymentRequestConverter paymentRequestConverter;

    @Override
    @Retry(name = RETRY_PAYMENT)
    @CircuitBreaker(name = CIRCUIT_PAYMENT, fallbackMethod = "fallbackPayment")
    public PaymentClientResponse requestPayment(PaymentClientRequest paymentClientRequest) {
        checkDuplicatePayment(paymentClientRequest);
        PaymentRequest request = paymentRequestConverter.convert(paymentClientRequest);

        ApiResponse<PaymentResponse> response = pgClient.requestPayment(request);

        return PaymentClientResponse.builder()
            .transactionKey(response.data().transactionKey())
            .status(TransactionStatus.valueOf(response.data().status().name()))
            .reason(response.data().reason())
            .build();
    }

    @Override
    public PaymentClientTransactionDetailResponse getTransaction(String transactionKey) {
        ApiResponse<TransactionDetailResponse> transaction = pgClient.getTransaction(transactionKey);
        return paymentRequestConverter.convert(transaction.data());
    }

    private void checkDuplicatePayment(PaymentClientRequest paymentClientRequest) {
        try {
            ApiResponse<OrderResponse> orderPaymentStatus = pgClient.getTransactionsByOrder(paymentClientRequest.orderId());

            if (orderPaymentStatus.data() != null) {
                OrderResponse data = orderPaymentStatus.data();
                if (data.hasTransactions()) {
                    log.warn("이미 결제된 주문입니다. 주문번호: {}", paymentClientRequest.orderId());
                    throw new IllegalStateException("이미 결제된 주문입니다.");
                }
            }
            log.info("결제 내역이 없어 정상 진행 가능. 주문번호: {}", paymentClientRequest.orderId());
        } catch (FeignException.NotFound e) {
            log.info("결제건이 존재하지 않아 새로운 결제 진행. 주문번호: {}", paymentClientRequest.orderId());
        } catch (FeignException e) {
            log.error("PG 시스템 조회 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public PaymentClientResponse fallbackPayment(PaymentClientRequest request, Exception ex) {
        log.error("결제 요청 실패, fallback 실행. 요청: {}", request, ex);
        return PaymentClientResponse.fail(ex.getMessage());
    }
}
