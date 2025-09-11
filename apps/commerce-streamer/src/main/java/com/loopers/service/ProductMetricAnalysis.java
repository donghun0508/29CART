package com.loopers.service;

import lombok.Getter;

@Getter
public class ProductMetricAnalysis {

    private final String type;
    private final String payload;

    public ProductMetricAnalysis(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

}
