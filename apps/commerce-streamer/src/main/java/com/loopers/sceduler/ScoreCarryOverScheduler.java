package com.loopers.sceduler;

import com.loopers.service.ScoreCarryOverService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ScoreCarryOverScheduler {

    private final ScoreCarryOverService scoreCarryOverService;

    @Scheduled(cron = "0 50 23 * * *", zone = "Asia/Seoul")
    public void carryOverDailyScores() {
        scoreCarryOverService.carry();
    }
}
