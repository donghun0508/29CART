package com.loopers.application.heart.processor;

import com.loopers.domain.heart.Target;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HeartProcessor {

    private final List<HeartProcess> heartProcesses;

    public void addHeart(Target target) {
        HeartProcess heartProcess = getHeartProcess(target);
        heartProcess.addHeart(target.targetId());
    }

    public void unHeart(Target target) {
        HeartProcess heartProcess = getHeartProcess(target);
        heartProcess.unHeart(target.targetId());
    }

    private HeartProcess getHeartProcess(Target target) {
        return heartProcesses.stream()
            .filter(process -> process.supports(target.targetType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 TargetType입니다: " + target.targetType()));
    }
}
