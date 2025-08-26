package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.shared.ProductItem;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class ProductItemFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private InstancioApi<List<ProductItem>> api;  // 타입 변경
        static final Integer MIN_TEST_SIZE = 1;
        static final Integer MAX_TEST_SIZE = 10;

        public Builder() {
            this.api =
                Instancio.ofList(ProductItem.class)
                    .generate(Select.root(), gen -> gen.collection()
                        .minSize(MIN_TEST_SIZE)
                        .maxSize(MAX_TEST_SIZE))
                    .generate(field(ProductItem::productId), gen -> gen.longs().min(1L).max(1000L))
                    .generate(field(ProductItem::unitPrice), gen -> gen.longs().min(1000L).max(100000L))
                    .generate(field(ProductItem::quantity), gen -> gen.longs().min(1L).max(10L));
        }

        public Builder productItems(int size) {
            this.api = this.api.generate(Select.root(), gen -> gen.collection().size(size));
            return this;
        }

        public List<ProductItem> build() {
            return api.create();
        }
    }
}
