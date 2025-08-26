package com.loopers.fixture;

import com.loopers.domain.catalog.Brand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class BrandFixture {

    public static Builder persistence() {
        return new Builder()
                .id(null);
    }

    public static class Builder {
        private final InstancioApi<Brand> api;

        public Builder() {
            this.api = Instancio.of(Brand.class)
                    .generate(field(Brand::getName), gen -> gen.string()
                            .prefix("Brand-")
                            .length(10)
                            .alphaNumeric());
        }

        public Builder id(Long id) {
            this.api.set(field(Brand::getId), id);
            return this;
        }

        public Brand build() {
            return api.create();
        }
    }
}
