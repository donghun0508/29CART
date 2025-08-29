package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.OrderCoupon;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class OrderCouponFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final InstancioApi<OrderCoupon> api;

        public Builder() {
            this.api = Instancio.of(OrderCoupon.class)
                .generate(field(OrderCoupon::paidPrice), gen -> gen.longs().min(1000L).max(100000L)
                    .as(Money::of))
            ;
        }

        public Builder issuedCouponId(Long issuedCouponId) {
            this.api.set(field(OrderCoupon::issuedCouponId), issuedCouponId);
            return this;
        }

        public Builder paidPrice(Money paidPrice) {
            this.api.set(field(OrderCoupon::paidPrice), paidPrice);
            return this;
        }

        public Builder paidPrice(Long paidPrice) {
            this.api.set(field(OrderCoupon::paidPrice), Money.of(paidPrice));
            return this;
        }

        public OrderCoupon build() {
            return api.create();
        }
    }

}
