package com.loopers.application.heart.processor;

import com.loopers.domain.heart.Target;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TargetProcessor {

    private final List<TargetProcess> targetProcesses;

    public void acquireTargetLock(Target target) {
        getHeartProcess(target).acquireTargetLock(target.targetId());
    }

    private TargetProcess getHeartProcess(Target target) {
        return targetProcesses.stream()
            .filter(process -> process.supports(target.targetType()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 TargetType입니다: " + target.targetType()));
    }
}
