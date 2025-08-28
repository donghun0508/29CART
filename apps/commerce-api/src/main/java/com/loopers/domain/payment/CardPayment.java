package com.loopers.domain.payment;

import static com.loopers.domain.payment.PaymentStatus.PENDING;
import static com.loopers.domain.payment.PaymentStatus.REQUESTED;

import com.loopers.domain.payment.PaymentClientData.PaymentClientResponse;
import com.loopers.domain.payment.PaymentEvent.CardPaymentRequestedEvent;
import com.loopers.domain.payment.PaymentEvent.PaymentCompletedEvent;
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

    public static CardPayment create(String orderNumber, Money paidAmount, CardType cardType, PaymentProvider paymentProvider) {
        CardPayment cardPayment = new CardPayment();

        cardPayment.orderNumber = orderNumber;
        cardPayment.paidAmount = paidAmount;
        cardPayment.cardType = cardType;
        cardPayment.status = PENDING;
        cardPayment.paymentProvider = paymentProvider;
        return cardPayment;
    }

    public void request() {
        if (this.status != PENDING) {
            throw new IllegalStateException("결제 대기 상태가 아닙니다.");
        }
        this.status = REQUESTED;
        this.registerEvent(new CardPaymentRequestedEvent(this));
    }

    public void complete(PaymentClientResponse paymentClientResponse) {
        this.providerResponse = paymentClientResponse;
        this.transactionId = paymentClientResponse.transactionKey();
        super.complete();
    }
}
