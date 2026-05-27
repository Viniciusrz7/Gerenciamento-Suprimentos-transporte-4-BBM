package com.bbm4.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    boolean nullable() default true;
    boolean unique() default false;
    int length() default 255;
}