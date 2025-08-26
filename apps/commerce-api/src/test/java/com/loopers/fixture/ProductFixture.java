package com.loopers.fixture;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.HeartCount;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.Stock;
import com.loopers.domain.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class ProductFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder persistence() {
        return new Builder()
                .id(null);
    }

    public static class Builder {

        private final InstancioApi<Product> api;

        public Builder() {
            this.api = Instancio.of(Product.class)
                    .generate(field(Product::getUnitPrice), gen -> gen.longs()
                            .min(1000L)
                            .max(100000L)
                            .as(Money::of))
                    .generate(field(Product::getStock), gen -> gen.longs()
                            .min(1L)
                            .max(1000L)
                            .as(Stock::of));
        }

        public Builder id(Long id) {
            this.api.set(field(Product::getId), id);
            return this;
        }

        public Builder stock(Stock stock) {
            this.api.set(field(Product::getStock), stock);
            return this;
        }

        public Builder price(Money price) {
            this.api.set(field(Product::getUnitPrice), price);
            return this;
        }

        public Builder brand(Brand brand) {
            this.api.set(field(Product::getBrand), brand);
            return this;
        }

        public Builder like(HeartCount heartCount) {
            this.api.set(field(Product::getHeartCount), heartCount);
            return this;
        }

        public Product build() {
            return this.api.create();
        }
    }
}
