package com.loopers.interfaces.consumer;

import static com.loopers.utils.ConverterUtil.header;

import com.loopers.service.ProductMetricAnalysis;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricConverter implements Converter<ConsumerRecord<String, byte[]>, ProductMetricAnalysis> {

    @Override
    public ProductMetricAnalysis convert(ConsumerRecord<String, byte[]> source) {
        Headers h = source.headers();

        String payloadJson = source.value() != null
            ? new String(source.value(), StandardCharsets.UTF_8)
            : null;

        return new ProductMetricAnalysis(header(h, "eventType"), payloadJson);
    }
}
