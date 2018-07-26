package com.zxsoft.fanfanfamily.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "cc")
public class AppCrossOriginProperties {

    private List<String> locations = new ArrayList<>();

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
