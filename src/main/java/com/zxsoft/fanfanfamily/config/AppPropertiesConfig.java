package com.zxsoft.fanfanfamily.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/*
* 集中程序集的配置文件中配置值，根据application.properties中的spring.profiles.active判断取prod/dev
*
*
 */

@Configuration
//@ConfigurationProperties(prefix = "com.zxsoft.fanfanfamily")
public class AppPropertiesConfig {


    @Value("${key}")
    private String contentKey;

    @Value("${RandomNumber}")
    private int randomInt;
    @Value("${RandomString}")
    private String randomString;

    //hibernate
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String hbm2ddl_auto;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernate_dialect;

    //Druid.datasource
    @Value("${Druid.datasource.url}")
    private String db_url;
    @Value("${Druid.datasource.username}")
    private String db_userName;
    @Value("${Druid.datasource.password}")
    private String db_password;
    @Value("${Druid.datasource.driver-class-name}")
    private String db_driverName;
    @Value("${Druid.datasource.dbcp2.pool-prepared-statements}")
    private boolean poolpreparedstatements;
    @Value("${Druid.tomcat.initial-size}")
    private int initialsize;
    @Value("${Druid.tomcat.max-active}")
    private int maxactive;
    @Value("${Druid.tomcat.min-idle}")
    private int minidle;
    @Value("${Druid.tomcat.max-wait}")
    private int maxwait;
    @Value("${Druid.tomcat.validation-query}")
    private String validationquery;
    @Value("${Druid.tomcat.test-on-borrow}")
    private boolean testonborrow;
    @Value("${Druid.tomcat.test-while-idle}")
    private boolean testwhileidle;
    @Value("${AppNameSpace}")
    private String appNameSpace;
    @Value("${uploadPath}")
    private String uploadPath;
    @Value("${fastjson.dateFormat}")
    private String fastJsonDateFormat;
    @Value("${RandomLessHundred}")
    private int randomLessHundred;
    @Value("${appDateFormat}")
    private String appDateFormat;
    @Value("${appShortDateFormat}")
    private String appShortDateFormat;

    @Autowired
    private AppCrossOriginProperties appCrossOriginProperties;

    public String getContentKey() {
        return contentKey;
    }

    public int getRandomInt() {
        return randomInt;
    }

    public String getRandomString() {
        return randomString;
    }

    public String isHbm2ddl_auto() {
        return hbm2ddl_auto;
    }

    public String getHibernate_dialect() {
        return hibernate_dialect;
    }

    public String getDb_url() {
        return db_url;
    }

    public String getDb_userName() {
        return db_userName;
    }

    public String getDb_password() {
        return db_password;
    }

    public String getDb_driverName() {
        return db_driverName;
    }

    public String getHbm2ddl_auto() {
        return hbm2ddl_auto;
    }
    public boolean isPoolpreparedstatements() {
        return poolpreparedstatements;
    }

    public int getInitialsize() {
        return initialsize;
    }

    public int getMaxactive() {
        return maxactive;
    }

    public int getMinidle() {
        return minidle;
    }

    public int getMaxwait() {
        return maxwait;
    }

    public String getValidationquery() {
        return validationquery;
    }

    public boolean isTestonborrow() {
        return testonborrow;
    }

    public boolean isTestwhileidle() {
        return testwhileidle;
    }
    public String getAppNameSpace() {
        return appNameSpace;
    }

    //集中配置属性，允许跨域访问的数组集
    public List<String> getCrossOriginLocations() {
        return appCrossOriginProperties.getLocations();
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public String getFastJsonDateFormat() {
        return fastJsonDateFormat;
    }

    public int getRandomLessHundred() {
        return randomLessHundred;
    }

    public String getAppDateFormat() {
        return appDateFormat;
    }

    public String getAppShortDateFormat() {
        return appShortDateFormat;
    }
}
