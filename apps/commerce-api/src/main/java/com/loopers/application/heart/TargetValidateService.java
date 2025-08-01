package com.loopers.application.heart;

import com.loopers.application.heart.validate.TargetValidate;
import com.loopers.domain.command.heart.Target;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class TargetValidateService {

    private final List<TargetValidate> targetValidates;

    void validate(Target target) {
        for (TargetValidate targetValidate : targetValidates) {
            if (targetValidate.supports(target.targetType())) {
                targetValidate.validTargetId(target.targetId());
                return;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 TargetType입니다: " + target.targetType());
    }
}
