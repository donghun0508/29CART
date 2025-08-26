package com.loopers.domain.catalog;

import static com.loopers.domain.shared.Preconditions.requireNonEmpty;
import static java.util.Objects.isNull;

import com.loopers.domain.shared.ProductItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inventory {

    private final List<Product> products;
    private final Map<Long, Stock> purchaseProducts;
    private final List<ProductItem> items;

    private Inventory(List<Product> products, Map<Long, Stock> purchaseProducts) {
        this.products = products;
        this.purchaseProducts = purchaseProducts;
        this.items = new ArrayList<>();
    }

    public static Inventory allocate(List<Product> products, Map<Long, Stock> purchaseProducts) {
        requireNonEmpty(products, "ProductManager.assign().products: 상품 목록은 비어있을 수 없습니다.");
        requireNonEmpty(purchaseProducts, "ProductManager.assign().purchaseProducts: 구매할 상품 목록은 비어있을 수 없습니다.");
        return new Inventory(products, purchaseProducts);
    }

    public void check() {
        for (Product product : products) {
            Stock purchaseQuantity = purchaseProducts.get(product.getId());
            if (isNull(purchaseQuantity)) {
                throw new IllegalStateException("ProductManager.decreaseStock(): 구매할 상품이 존재하지 않습니다.");
            }
            if (product.getStock().isLessThan(purchaseQuantity)) {
                throw new IllegalStateException(
                    "ProductManager.decreaseStock(): 재고가 부족합니다. 상품: " + product.getName() + ", 재고: " + product.getStock()
                        + ", 구매 수량: " + purchaseQuantity);
            }
            items.add(ProductItem.of(product, purchaseQuantity));
        }
    }

    public void decreaseStock() {
        for (Product product : products) {
            Stock purchaseQuantity = purchaseProducts.get(product.getId());
            if (isNull(purchaseQuantity)) {
                throw new IllegalStateException("ProductManager.decreaseStock(): 구매할 상품이 존재하지 않습니다.");
            }
            product.decreaseStock(purchaseQuantity);
        }
    }

    public List<ProductItem> items() {
        return items;
    }
}
