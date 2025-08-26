package com.loopers.application.heart.processor;

import com.loopers.domain.heart.TargetType;

public interface HeartProcess {

    boolean supports(TargetType targetType);

    void addHeart(Long targetId);

    void unHeart(Long targetId);
}
