package com.loopers.domain;

import com.loopers.common.domain.CreatedOnlyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class ProductMetric extends CreatedOnlyEntity {

    private Long productId;
    private Long viewCount;
    private Long likeCount;
    private Long purchaseCount;

    public static ProductMetric of(Long productId) {
        return ProductMetric.builder()
            .productId(productId)
            .viewCount(0L)
            .likeCount(0L)
            .purchaseCount(0L)
            .build();
    }

    public void incrementDetailViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void increaseStock(Long stock) {
        this.purchaseCount += stock;
    }

    public void decreaseStock(Long stock) {
        this.purchaseCount -= stock;
    }
}
