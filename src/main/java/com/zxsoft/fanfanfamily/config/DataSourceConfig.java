package com.zxsoft.fanfanfamily.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.zxsoft.fanfanfamily.mort.repository"})
@EnableTransactionManagement()
public class DataSourceConfig  implements TransactionManagementConfigurer {
    @Autowired
    AppPropertiesConfig propConfig;

    @Bean
    public DataSource dataSource() {

//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        return builder.setType(EmbeddedDatabaseType.HSQL).build();
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(propConfig.getDb_url());
        dataSource.setUsername(propConfig.getDb_userName());//用户名
        dataSource.setPassword(propConfig.getDb_password());//密码

        dataSource.setInitialSize(propConfig.getInitialsize());
        dataSource.setMaxActive(propConfig.getMaxactive());
        dataSource.setMinIdle(propConfig.getMinidle());
        dataSource.setMaxWait(propConfig.getMaxwait());
        dataSource.setValidationQuery(propConfig.getValidationquery());
        dataSource.setTestOnBorrow(propConfig.isTestonborrow());
        dataSource.setTestWhileIdle(propConfig.isTestwhileidle());
        dataSource.setPoolPreparedStatements(propConfig.isPoolpreparedstatements());

        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(String.format("%s.mort.domain", propConfig.getAppNameSpace()),
                String.format("%s.base.domain", propConfig.getAppNameSpace()));
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean//(name = "JpaTransactionManager")
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    //保留。后续加入多个事务管理器后生效
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
}
