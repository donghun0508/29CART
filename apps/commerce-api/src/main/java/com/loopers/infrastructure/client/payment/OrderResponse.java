package com.loopers.infrastructure.client.payment;

import java.util.List;

public record OrderResponse(String orderId, List<TransactionResponse> transactions) {

    public boolean hasTransactions() {
        return transactions != null && !transactions.isEmpty();
    }
}
