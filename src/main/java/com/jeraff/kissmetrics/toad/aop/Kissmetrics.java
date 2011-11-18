package com.jeraff.kissmetrics.toad.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Kissmetrics {
    Record[] value();
    Record[] record();
    Set[] set();
    Alias[] alias();
}
