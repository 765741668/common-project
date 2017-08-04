package com.yz.datasources;

import java.lang.annotation.*;

/**
 * Created by yangzhao on 17/2/7.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default "defaultSource";
}
