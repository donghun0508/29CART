package com.loopers.domain.payment;

import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentEvent.CardPaymentCreatedEvent;
import com.loopers.domain.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "card_payment")
@DiscriminatorValue("CARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardPayment extends Payment {

    @Enumerated(EnumType.STRING)
    @Column(
        name = "card_type",
        nullable = false,
        updatable = false
    )
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, updatable = false)
    private PaymentProvider paymentProvider;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "provider_response", columnDefinition = "JSON")
    private PaymentClientResponse providerResponse;

    @Column(name = "transaction_id")
    private String transactionId;

    public static CardPayment initiate(String orderNumber, Money paidAmount, CardType cardType, PaymentProvider paymentProvider,
        PaymentClientResponse providerResponse, String transactionId) {
        CardPayment cardPayment = new CardPayment();

        cardPayment.orderNumber = orderNumber;
        cardPayment.paidAmount = paidAmount;
        cardPayment.status = PaymentStatus.REQUESTED;

        cardPayment.cardType = cardType;
        cardPayment.paymentProvider = paymentProvider;
        cardPayment.providerResponse = providerResponse;
        cardPayment.transactionId = transactionId;

        cardPayment.registerEvent(new CardPaymentCreatedEvent(cardPayment));

        return cardPayment;
    }

    public static CardPayment failCard(String orderNumber, Money paidAmount, CardType cardType, PaymentProvider paymentProvider,
        PaymentClientResponse providerResponse) {
        CardPayment cardPayment = new CardPayment();

        cardPayment.orderNumber = orderNumber;
        cardPayment.paidAmount = paidAmount;
        cardPayment.status = PaymentStatus.FAILED;
        cardPayment.cardType = cardType;
        cardPayment.paymentProvider = paymentProvider;
        cardPayment.providerResponse = providerResponse;

        cardPayment.registerEvent(new CardPaymentCreatedEvent(cardPayment));

        return cardPayment;
    }
}
