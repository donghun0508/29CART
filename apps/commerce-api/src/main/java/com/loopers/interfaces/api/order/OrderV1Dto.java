package com.loopers.interfaces.api.order;

import com.loopers.domain.command.order.OrderForm;
import java.util.List;

public class OrderV1Dto {

    public static class RequestOrder {

        public record Request(
            List<PurchaseItem> items
        ) {

            public OrderForm toOrderForm() {
                return OrderForm.builder()
                    .purchaseItems(
                        items.stream().map(item ->
                                new OrderForm.PurchaseItem(item.productId, item.quantity))
                            .toList()
                    )
                    .build();
            }

            public record PurchaseItem(
                Long productId,
                Long quantity
            ) {

            }
        }
    }

}
