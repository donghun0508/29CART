package com.loopers.service.strategy.ranking;

import static com.loopers.service.strategy.ranking.HotProductKeyGenerator.HotProductKeyScope.HOURLY;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HourlyHotProductKeyGenerator implements HotProductKeyGenerator {

    private static final DateTimeFormatter HOUR_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHH");

    @Override
    public boolean isSupport(HotProductKeyScope scope) {
        return HOURLY.equals(scope);
    }

    public String generate() {
        return "ranking:all:" + LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(HOUR_FMT);
    }

    @Override
    public String next() {
        return "ranking:all:" + LocalDateTime.now(ZoneId.of("Asia/Seoul")).plusHours(1).format(HOUR_FMT);
    }
}
