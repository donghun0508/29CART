package com.loopers.application.catalog;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HotProductKeyGenerator {
    private static final String KEY_PREFIX = "ranking:all:";
    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.BASIC_ISO_DATE;

    public String generate() {
        return KEY_PREFIX + LocalDate.now(ZoneId.of("Asia/Seoul")).format(DAY_FMT);
    }

    public String generate(String date) {
        return KEY_PREFIX + date;
    }

    public String generate(LocalDate date) {
        return KEY_PREFIX + date.format(DAY_FMT);
    }
}
