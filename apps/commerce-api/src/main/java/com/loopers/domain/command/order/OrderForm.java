package com.loopers.domain.command.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

public class OrderForm {

    private final List<PurchaseItem> purchaseItems;
    @Getter private final Map<Long, Long> purchaseItemQuantityMap;

    @Builder
    private OrderForm(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
        this.purchaseItemQuantityMap = toProductQuantityMap();
    }

    public List<Long> productIds() {
        return purchaseItems.stream().map(p->p.productId).toList();
    }

    private Map<Long, Long> toProductQuantityMap() {
        return purchaseItems.stream()
            .collect(Collectors.toMap(
                PurchaseItem::productId,
                PurchaseItem::quantity
            ));
    }

    public Long getQuantity(Long productId) {
        return this.purchaseItemQuantityMap.get(productId);
    }

    public record PurchaseItem(Long productId, Long quantity) {

    }
}
