package com.loopers.application.order;


import static com.loopers.domain.shared.Preconditions.requireNonBlank;
import static com.loopers.domain.shared.Preconditions.requireNonEmpty;

import com.loopers.domain.catalog.Stock;
import com.loopers.domain.order.IdempotencyKey;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.user.AccountId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderCommand {

    public record OrderRequestCommand(
        AccountId accountId,
        IdempotencyKey idempotencyKey,
        Long couponId,
        Map<Long, Stock> purchaseProducts,
        PaymentMethod paymentMethod
    ) {

        public OrderRequestCommand(String accountId, String idempotencyKey, Long couponId, Map<Long, Long> productQuantities,
            PaymentMethod paymentMethod) {
            this(
                AccountId.of(requireNonBlank(accountId, "회원 ID는 비어있을 수 없습니다."))
                , IdempotencyKey.of(idempotencyKey)
                , couponId
                , requireNonEmpty(productQuantities, "구매할 상품 목록은 비어있을 수 없습니다.")
                    .entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> Stock.of(entry.getValue())))
                , paymentMethod
            );
        }

        public static OrderRequestCommand of(String accountId, String idempotencyKey, Long countId, Map<Long, Long> purchaseMap,
            PaymentMethod paymentMethod) {
            return new OrderRequestCommand(accountId, idempotencyKey, countId, purchaseMap,
                paymentMethod);
        }

        public List<Long> productIds() {
            return purchaseProducts.keySet().stream().toList();
        }

        public boolean applyCoupon() {
            return couponId != null;
        }
    }
}
