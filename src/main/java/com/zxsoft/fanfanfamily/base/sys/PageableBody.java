package com.zxsoft.fanfanfamily.base.sys;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@FanfAppBody
public @interface PageableBody {
    @AliasFor("value")
    boolean value() default true;
}
