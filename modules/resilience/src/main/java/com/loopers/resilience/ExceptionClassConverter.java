package com.loopers.resilience;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExceptionClassConverter {

    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] convertToClasses(List<String> classNames) {
        return classNames.stream()
            .map(this::loadClass)
            .toArray(Class[]::new);
    }

    @SuppressWarnings("unchecked")
    Class<? extends Throwable> loadClass(String className) {
        try {
            return (Class<? extends Throwable>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot load exception class: " + className, e);
        }
    }
}
