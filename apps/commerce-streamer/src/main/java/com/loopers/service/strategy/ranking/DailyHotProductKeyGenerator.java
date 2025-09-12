package com.loopers.service.strategy.ranking;

import static com.loopers.service.strategy.ranking.HotProductKeyGenerator.HotProductKeyScope.DAILY;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DailyHotProductKeyGenerator implements HotProductKeyGenerator {

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.BASIC_ISO_DATE;

    @Override
    public boolean isSupport(HotProductKeyScope scope) {
        return DAILY.equals(scope);
    }

    public String generate() {
        return "ranking:all:" + LocalDate.now(ZoneId.of("Asia/Seoul")).format(DAY_FMT);
    }

    @Override
    public String next() {
        return "ranking:all:" + LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1).format(DAY_FMT);
    }
}
