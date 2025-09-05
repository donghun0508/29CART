package com.loopers.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.service.strategy.ProductMetricIncreaseLikeStrategy.ProductHeartIncreasedEvent;
import org.junit.jupiter.api.Test;


class PayloadMapperTest {
    @Test
    void testMapPayload() {
        PayloadMapper mapper = new PayloadMapper(new ObjectMapper());
        String payload = "{\"productId\": 31, \"heartCount\": 6}";
        ProductHeartIncreasedEvent productHeartIncreasedEvent = mapper.parsePayload(payload, ProductHeartIncreasedEvent.class);
        assertEquals(31L, productHeartIncreasedEvent.productId());
    }
}
