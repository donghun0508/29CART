package com.loopers.domain.payment;

public sealed interface PaymentMethod permits CardMethod, PointMethod {

}
