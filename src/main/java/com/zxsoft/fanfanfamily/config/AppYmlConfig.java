package com.zxsoft.fanfanfamily.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
//@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "fanfanfamily")
public class AppYmlConfig {
//    @Value("${fanfanfamily.BannedFiles}")
//    private List<String> BannedFiles;
//
//    public List<String> getBannedFiles() {
//        return BannedFiles;
//    }

//    @Value("${fanfanfamily}")
//    private String fanfanfamily;

//    @Value("${fanfanfamily}")
    private List<String> BannedFiles;

    public List<String> getBannedFiles() {
        return BannedFiles;
    }

    public void setBannedFiles(List<String> bannedFiles) {
        BannedFiles = bannedFiles;
    }
}
