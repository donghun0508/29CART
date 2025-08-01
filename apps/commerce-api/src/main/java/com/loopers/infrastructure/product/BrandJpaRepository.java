package com.loopers.infrastructure.product;

import com.loopers.domain.command.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<Brand, Long>, CustomBrandQueryRepository {

}
