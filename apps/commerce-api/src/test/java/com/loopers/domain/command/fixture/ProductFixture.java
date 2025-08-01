package com.loopers.domain.command.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.command.product.Product;
import com.loopers.domain.command.product.Stock;
import com.loopers.domain.command.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class ProductFixture {

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static ProductBuilder integration() {
        return new ProductBuilder()
            .withId(null);
    }

    public static class ProductBuilder {

        private InstancioApi<Product> api;

        public ProductBuilder() {
            this.api = Instancio.of(Product.class);
        }

        public ProductBuilder withId(Long id) {
            this.api = this.api.set(field(Product::getId), id);
            return this;
        }

        public ProductBuilder withStock(Long stock) {
            this.api = this.api.set(field(Stock::count), stock);
            return this;
        }

        public ProductBuilder withPrice(long price) {
            this.api = this.api.set(field(Money::value), price);
            return this;
        }

        public Product build() {
            return this.api.create();
        }
    }
}
