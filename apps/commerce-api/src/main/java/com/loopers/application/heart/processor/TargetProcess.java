package com.loopers.application.heart.processor;

import com.loopers.domain.heart.TargetType;

public interface TargetProcess {

    boolean supports(TargetType targetType);

    void acquireTargetLock(Long targetId);
}
