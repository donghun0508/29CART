package com.loopers.infrastructure.product;

import com.loopers.domain.command.product.Product;
import com.loopers.domain.query.product.ProductQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long>, CustomProductQueryRepository {

}
