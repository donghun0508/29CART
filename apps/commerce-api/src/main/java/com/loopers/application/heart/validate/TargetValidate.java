package com.loopers.application.heart.validate;

import com.loopers.domain.command.heart.TargetType;

public interface TargetValidate {
    boolean supports(TargetType targetType);
    void validTargetId(Long targetId);
}
