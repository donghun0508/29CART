package com.loopers.domain.catalog;

import com.loopers.domain.heart.Target;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRead {

    private Long id;
    private String name;
    private Long unitPrice;
    private Long stock;
    private Long heartCount;
    private Long brandId;
    private String brandName;
    private boolean isSoldOut;
    private boolean isLiked;

    private ProductRead(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.unitPrice = product.getUnitPrice().value();
        this.stock = product.getStock().count();
        this.heartCount = product.getHeartCount();
        this.brandId = product.getBrand().getId();
        this.brandName = product.getBrand().getName();
        this.isSoldOut = product.isSoldOut();
    }

    public static ProductRead from(Product product) {
        ProductRead productRead = new ProductRead(product);
        productRead.isLiked = false;
        return productRead;
    }

    public Target toTarget() {
        return Target.asProduct(this.id);
    }
}
