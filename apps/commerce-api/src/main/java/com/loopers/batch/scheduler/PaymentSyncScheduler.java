package com.loopers.batch.scheduler;

import static java.util.Objects.nonNull;

import com.loopers.application.payment.PaymentCommand.PaymentSyncCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClientData.PaymentClientTransactionDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentSyncScheduler {

    private final PaymentClient paymentClient;
    private final PaymentFacade paymentFacade;
    private final PaymentSyncConverter paymentSyncConverter;

    @Scheduled(
        fixedRateString = "${external.payment.providers.simulator.sync-interval}",
        initialDelayString = "${external.payment.providers.simulator.initial-delay}"
    )
    public void syncPaymentStatus() {
        List<String> transactionKeys = paymentFacade.getSyncTransactionIds();
        for (String transactionKey : transactionKeys) {
            PaymentClientTransactionDetailResponse response = paymentClient.getTransaction(transactionKey);
            if (nonNull(response)) {
                PaymentSyncCommand paymentSyncCommand = paymentSyncConverter.convert(response);
                paymentFacade.syncPayment(paymentSyncCommand);
            }
        }
    }
}
