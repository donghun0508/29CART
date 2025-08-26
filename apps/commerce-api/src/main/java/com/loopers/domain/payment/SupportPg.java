package com.loopers.domain.payment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public class SupportPg {

    @Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Component
    @Qualifier
    public @interface PgSimulator {

    }

}
