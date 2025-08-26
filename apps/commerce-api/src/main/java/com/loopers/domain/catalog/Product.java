package com.loopers.domain.catalog;

import static com.loopers.domain.shared.Preconditions.requireNonNull;
import static com.loopers.domain.shared.Preconditions.requirePositive;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String name;

    @AttributeOverride(name = "value", column = @Column(name = "unit_price", nullable = false))
    private Money unitPrice;

    @AttributeOverride(name = "count", column = @Column(name = "stock", nullable = false))
    private Stock stock;

    @AttributeOverride(name = "value", column = @Column(name = "heart_count", nullable = false))
    private HeartCount heartCount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Version
    private Long version;

    public void decreaseStock(Stock stock) {
        requireNonNull(stock, "Product.decreaseStock().stock: 수량은 null일 수 없습니다.");
        requirePositive(stock.count(), "Product.decreaseStock().stock.value: 수량은 0보다 커야 합니다.");
        this.stock = this.stock.subtract(stock);
    }

    public void heart() {
        this.heartCount = this.heartCount.up();
    }

    public void unHeart() {
        this.heartCount = this.heartCount.down();
    }

    public boolean isSoldOut() {
        return this.stock.isZero();
    }

    public Long getHeartCount() {
        return this.heartCount.value();
    }
}
