package com.loopers.domain.shared;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.order.Quantity;
import java.util.List;

public class StockReservations {

    private final List<StockReservation> stockReservations;

    public StockReservations(List<StockReservation> stockReservations) {
        this.stockReservations = stockReservations;
    }

    public Money totalPrice() {
        return stockReservations.stream()
            .map(StockReservation::totalPrice)
            .reduce(Money.ZERO, Money::add);
    }

    public List<StockReservation> getStockReservations() {
        return stockReservations;
    }

    public record StockReservation(Long productId, Money price, Quantity quantity) {
        public StockReservation {
            requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
            requireNonNull(price, "가격은 null일 수 없습니다.");
            requireNonNull(quantity, "수량은 null일 수 없습니다.");
        }

        public StockReservation(Long productId, Long price, Long quantity) {
            this(
                requireNonNull(productId, "상품 ID는 null일 수 없습니다."),
                Money.of(requireNonNull(price, "가격은 null일 수 없습니다.")),
                Quantity.of(requireNonNull(quantity, "수량은 null일 수 없습니다."))
            );
        }

        public static StockReservation of(Long productId, Long price, Long quantity) {
            return new StockReservation(productId, Money.of(price), Quantity.of(quantity));
        }

        public Money totalPrice() {
            return price.multiply(quantity.count());
        }
    }
}
