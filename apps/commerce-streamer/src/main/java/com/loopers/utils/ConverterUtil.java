package com.loopers.utils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.kafka.common.header.Headers;

public class ConverterUtil {
    public static String header(Headers h, String key) {
        var v = h != null ? h.lastHeader(key) : null;
        return v == null ? null : new String(v.value(), StandardCharsets.UTF_8);
    }

    public static Integer parseInt(String s) {
        try { return s == null ? null : Integer.valueOf(s); } catch (Exception e) { return null; }
    }

    public static String headersAsJson(Headers h) {
        if (h == null) return null;
        Map<String, String> map = new LinkedHashMap<>();
        h.forEach(head -> map.put(head.key(), new String(head.value(), StandardCharsets.UTF_8)));
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return null;
        }
    }
}
