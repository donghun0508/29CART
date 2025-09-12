package com.loopers.domain;

import com.loopers.common.domain.AggregateRoot;
import com.loopers.domain.ProductMetricEvent.ProductDecrementedLikeEvent;
import com.loopers.domain.ProductMetricEvent.ProductDecrementedStockEvent;
import com.loopers.domain.ProductMetricEvent.ProductIncreasedStockEvent;
import com.loopers.domain.ProductMetricEvent.ProductIncrementedLikeEvent;
import com.loopers.domain.ProductMetricEvent.ProductIncrementedViewEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(
    name = "product_metrics"
)
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ProductMetric extends AggregateRoot {

    private Long productId;
    private Long viewCount;
    private Long likeCount;
    private Long purchaseCount;
    private Long unitPrice;

    public static ProductMetric of(Long productId, Long unitPrice) {
        return ProductMetric.builder()
            .productId(productId)
            .viewCount(0L)
            .likeCount(0L)
            .purchaseCount(0L)
            .unitPrice(unitPrice)
            .build();
    }

    public void incrementDetailViewCount() {
        this.viewCount++;
        this.registerEvent(new ProductIncrementedViewEvent(this, 1L));
    }

    public void incrementLikeCount() {
        this.likeCount++;
        this.registerEvent(new ProductIncrementedLikeEvent(this, 1L));
    }

    public void decrementLikeCount() {
        this.likeCount--;
        this.registerEvent(new ProductDecrementedLikeEvent(this, -1L));
    }

    public void increaseStock(Long stock) {
        this.purchaseCount += stock;
        this.registerEvent(new ProductIncreasedStockEvent(this, stock, unitPrice * stock));
    }

    public void decreaseStock(Long stock) {
        this.purchaseCount -= stock;
        this.registerEvent(new ProductDecrementedStockEvent(this, stock, -(unitPrice * stock)));
    }
}
