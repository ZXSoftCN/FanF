package com.zxsoft.fanfanfamily.base.sys;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseBody
public @interface FanfAppBody {
    @AliasFor("value")
    boolean value() default true;
}
