package com.loopers.interfaces.api.order;

import static java.util.Objects.isNull;

import com.loopers.application.payment.data.CardMethod;
import com.loopers.application.payment.data.PaymentMethod;
import com.loopers.application.payment.data.PointMethod;
import com.loopers.domain.payment.CardNumber;
import com.loopers.domain.payment.CardType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderV1Dto {

    public record OrderRequest(Long couponId, String idempotencyKey, List<PurchaseItem> items, CardPaymentRequest card) {

        public record PurchaseItem(Long productId, Long quantity) {

        }

        public record CardPaymentRequest(String number, CardType type) {

            public static CardPaymentRequest of(String number, CardType type) {
                return new CardPaymentRequest(number, type);
            }
        }

        public Map<Long, Long> toPurchaseMap() {
            return items.stream()
                .collect(Collectors.toMap(PurchaseItem::productId, PurchaseItem::quantity));
        }

        public PaymentMethod paymentSpec() {
            if (isNull(card)) {
                return new PointMethod();
            } else {
                return new CardMethod(
                    CardType.valueOf(card.type().name()),
                    CardNumber.of(card.number())
                );
            }
        }
    }
}
