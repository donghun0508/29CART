package com.loopers.application.catalog;

import com.loopers.application.catalog.BrandResult.BrandDetailResult;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandService;
import com.loopers.domain.catalog.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandFacade {

    private final BrandService brandService;
    private final ProductService productService;

    public BrandDetailResult getBrandDetail(Long brandId) {
        Brand brand = brandService.getBrandDetail(brandId);
        Long productCount = productService.getCountByBrandId(brandId);
        return new BrandDetailResult(brand.getId(), brand.getName(), productCount);
    }
}
