package com.loopers.domain.command.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAllById(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    @Transactional(readOnly = true)
    public boolean existById(Long productId) {
        return productRepository.existsById(productId);
    }

    @Transactional
    public void saveAll(List<Product> purchaseProducts) {
        purchaseProducts.forEach(this::save);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
