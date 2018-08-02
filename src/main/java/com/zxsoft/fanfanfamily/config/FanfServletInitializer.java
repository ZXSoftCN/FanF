package com.zxsoft.fanfanfamily.config;

import com.zxsoft.fanfanfamily.FanfanfamilyApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class FanfServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FanfanfamilyApplication.class);
    }
}
