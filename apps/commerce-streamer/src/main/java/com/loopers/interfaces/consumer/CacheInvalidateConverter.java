package com.loopers.interfaces.consumer;

import static com.loopers.utils.ConverterUtil.header;

import com.loopers.service.CacheInvalidate;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidateConverter implements Converter<ConsumerRecord<String, byte[]>, CacheInvalidate> {

    @Override
    public CacheInvalidate convert(ConsumerRecord<String, byte[]> source) {
        Headers h = source.headers();

        String payloadJson = source.value() != null
            ? new String(source.value(), StandardCharsets.UTF_8)
            : null;

        return new CacheInvalidate(header(h, "eventType"), payloadJson);
    }
}
