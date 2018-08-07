package com.zxsoft.fanfanfamily.base.sys;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/*
    设定像素图片的缺省值
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface AvatorLoadFactorDefault {

        @AliasFor(value = "scaling")
        int value() default 1;

        @AliasFor(value = "value")
        int scaling() default 1;

        int width() default 15;

        int height() default 15;
}
