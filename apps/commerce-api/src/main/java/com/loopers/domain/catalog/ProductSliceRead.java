package com.loopers.domain.catalog;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class ProductSliceRead {

    private List<ProductRead> content;
    private boolean hasNext;
    private int size;
    private int number;

    public ProductSliceRead() {
    }

    public static ProductSliceRead from(List<Product> products) {
        ProductSliceRead productSliceRead = new ProductSliceRead();

        productSliceRead.content = products.stream()
            .map(ProductRead::from)
            .toList();

        productSliceRead.hasNext = false;
        productSliceRead.size = products.size();
        productSliceRead.number = 0;

        return productSliceRead;
    }

    public static ProductSliceRead from(Slice<Product> products) {
        ProductSliceRead productSliceRead = new ProductSliceRead();

        productSliceRead.content = products.getContent().stream()
            .map(ProductRead::from)
            .toList();
        productSliceRead.hasNext = products.hasNext();
        productSliceRead.size = products.getSize();
        productSliceRead.number = products.getNumber();

        return productSliceRead;
    }
}
