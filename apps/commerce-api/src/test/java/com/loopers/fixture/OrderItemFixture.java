package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.order.Quantity;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.StockReservations.StockReservation;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class OrderItemFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final InstancioApi<List<StockReservation>> api;  // 타입 변경
        static final Integer MIN_TEST_SIZE = 1;
        static final Integer MAX_TEST_SIZE = 10;

        public Builder() {
            this.api =
                Instancio.ofList(StockReservation.class)
                    .generate(Select.root(), gen -> gen.collection()
                        .minSize(MIN_TEST_SIZE)
                        .maxSize(MAX_TEST_SIZE))
                    .generate(field(StockReservation::productId), gen -> gen.longs().min(1L).max(1000L))
                    .generate(field(StockReservation::price), gen -> gen.longs().min(1000L).max(100000L)
                        .as(Money::of))
                    .generate(field(StockReservation::quantity), gen -> gen.longs().min(1L).max(10L)
                        .as(Quantity::of));
        }

        public Builder orderItems(int size) {
            this.api.generate(Select.root(), gen -> gen.collection().size(size));
            return this;
        }

        public List<StockReservation> build() {
            return api.create();
        }
    }
}
