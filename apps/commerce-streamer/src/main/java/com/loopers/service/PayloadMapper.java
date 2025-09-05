package com.loopers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayloadMapper {

    private final ObjectMapper om;

    public <T> T parsePayload(String payloadJson, Class<T> type) {
        try {
            if (payloadJson.startsWith("\"") && payloadJson.endsWith("\"")) {
                payloadJson = om.readValue(payloadJson, String.class);
            }
            return om.readValue(payloadJson, type);
        } catch (JsonProcessingException e) {
            log.error("[ProductMetricPayloadMapper.parsePayload] JsonProcessingException", e);
            throw new RuntimeException(e);
        }
    }

}
