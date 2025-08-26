package com.loopers.domain.catalog;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {

    Brand save(Brand brand);

    List<Brand> findAllById(List<Long> brandIds);

    Optional<Brand> findById(Long brandId);
}
