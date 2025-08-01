package com.loopers.domain.command.product;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.command.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String name;

    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @AttributeOverride(name = "count", column = @Column(name = "stock"))
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public void decreaseStock(Long quantity) {
        requireNonNull(quantity, "수량은 null일 수 없습니다.");
        this.stock = this.stock.subtract(new Stock(quantity));
    }
}
