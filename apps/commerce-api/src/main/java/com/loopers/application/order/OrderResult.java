package com.loopers.application.order;

import com.loopers.domain.order.OrderNumber;

public class OrderResult {

    public record OrderPaymentResult(OrderNumber orderNumber) {

    }

}
