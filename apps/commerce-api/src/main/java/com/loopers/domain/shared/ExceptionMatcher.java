package com.loopers.domain.shared;

import org.springframework.stereotype.Component;

@Component
public class ExceptionMatcher {

    public boolean isNotEnoughPoint(Exception e) {
        return e.getMessage() != null && e.getMessage().contains("Money.subtract().other");
    }

}
