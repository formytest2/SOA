package com.tranboot.client.druid.support.monitor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MField {
    String name() default "";

    boolean groupBy() default false;

    AggregateType aggregate();

    String hashFor() default "";

    String hashForType() default "";
}
