package com.loopers.application.payment.data;

public sealed interface PaymentMethod permits CardMethod, PointMethod {

}
