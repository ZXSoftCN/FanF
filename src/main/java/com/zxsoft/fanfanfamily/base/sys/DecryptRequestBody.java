package com.zxsoft.fanfanfamily.base.sys;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptRequestBody {

    boolean value() default true;
}
