package com.loopers.domain.catalog;

import com.loopers.domain.shared.StockReservations;
import com.loopers.domain.shared.StockReservations.StockReservation;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Products {

    private final List<Product> products;

    private Products(List<Product> products) {
        this.products = products;
    }

    public static Products from(List<Product> products) {
        return new Products(products);
    }

    public void ensureAllExist(List<Long> productIds) {
        List<Long> foundIds = this.products.stream()
            .map(Product::getId)
            .toList();
        List<Long> missingIds = productIds.stream()
            .filter(id -> !foundIds.contains(id))
            .toList();
        if (!missingIds.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품: " + missingIds);
        }
    }

    public StockReservations reserve(Map<Long, Stock> stockMap) {
        List<StockReservation> stockReservations = new ArrayList<>();
        for (Product product : products) {
            Stock stock = stockMap.get(product.getId());
            product.decreaseStock(stock);
            stockReservations.add(new StockReservation(product.getId(), product.getUnitPrice().value(), stock.count()));
        }
        return new StockReservations(stockReservations);
    }

    public void restore(Map<Long, Long> stockMap) {
        for (Product product : products) {
            Long count = stockMap.get(product.getId());
            product.increaseStock(Stock.of(count));
        }
    }
}
