package com.loopers.service;

import lombok.Getter;

@Getter
public class ProductMetricAnalysis {

    private final String type;
    private final String eventId;
    private final String payload;

    public ProductMetricAnalysis(String type, String eventId, String payload) {
        this.type = type;
        this.eventId = eventId;
        this.payload = payload;
    }

}
