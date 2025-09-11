package com.loopers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductMetricService {

    private final ProductMetricTemplate productMetricTemplate;

    public void save(ProductMetricAnalysis productMetricAnalysis) {
        productMetricTemplate.execute(productMetricAnalysis);
    }
}
