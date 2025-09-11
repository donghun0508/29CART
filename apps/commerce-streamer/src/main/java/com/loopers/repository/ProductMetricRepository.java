package com.loopers.repository;

import com.loopers.domain.ProductMetric;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMetricRepository extends JpaRepository<ProductMetric, Long> {

    Optional<ProductMetric> findByProductId(Long productId);
}
