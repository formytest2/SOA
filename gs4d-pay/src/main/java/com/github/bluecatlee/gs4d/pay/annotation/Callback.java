package com.github.bluecatlee.gs4d.pay.annotation;

import java.lang.annotation.*;

/**
 * 标识方法是回调方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Callback {
}
