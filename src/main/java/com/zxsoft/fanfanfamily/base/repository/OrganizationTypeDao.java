package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
*   原本应该放在com.zxsoft.fanfanfamily.base.repository包下，
*   单元测试时，可spring扫描加载不到。
*   直接指定@ComponentScan加载都不行。存疑！
*
*   已处理：
*   respository类，还需要通过@EnableJpaRepositories扫描加载进来。
*   在DataSourceConfig类上
 */
public interface OrganizationTypeDao extends JpaRepository<OrganizationType,String> {

    Optional<OrganizationType> findFirstByNameContaining(String name);
}
