package com.loopers.infrastructure.persistence.catalog;

import com.loopers.domain.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {

}
