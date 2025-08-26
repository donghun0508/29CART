package com.loopers.infrastructure.persistence.catalog;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Brand save(Brand brand) {
        return brandJpaRepository.save(brand);
    }

    @Override
    public List<Brand> findAllById(List<Long> brandIds) {
        return brandJpaRepository.findAllById(brandIds);
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return brandJpaRepository.findById(brandId);
    }
}
