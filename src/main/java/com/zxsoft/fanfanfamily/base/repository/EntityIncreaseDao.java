package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
public interface EntityIncreaseDao extends JpaRepository<EntityIncrease,String> {

    Optional<EntityIncrease> findFirstByEntityNameEquals(String entityName);
    Optional<EntityIncrease> findFirstByEntityNameIgnoreCase(String entityName);

    Page<EntityIncrease> findEntityIncreaseByNameContaining(String name, Pageable pageable);

    @Query("select max(u.sortNoMax) from EntityIncrease u where upper(u.entityName) = upper(?1) ")
    Optional<Integer> getSortNoMax(String entityName);
    @Query("select max(u.sortNoMax) from EntityIncrease u where u = ?1 ")
    Optional<Integer> getSortNoMax(EntityIncrease t);

    @Query("select max(u.codeNumMax) from EntityIncrease u where upper(u.entityName) = upper(?1) ")
    Optional<Integer> getCodeNumMax(String entityName);
    @Query("select max(u.codeNumMax) from EntityIncrease u where u = ?1 ")
    Optional<Integer> getCodeNumMax(EntityIncrease t);

    @Transactional
    @Modifying
    @Query("update EntityIncrease u set u.codeNumMax = ?2 where upper(u.entityName) = upper(?1)")
    void updateCodeNumMax(String entityName,int numMax);
    @Transactional
    @Modifying
    @Query("update EntityIncrease u set u.codeNumMax = ?2 where u = ?1")
    void updateCodeNumMax(EntityIncrease t,int numMax);

    @Transactional
    @Modifying
    @Query("update EntityIncrease u set u.sortNoMax = ?2 where upper(u.entityName) = upper(?1)")
    void updateSortNoMax(String entityName,int sortNoMax);
    @Transactional
    @Modifying
    @Query("update EntityIncrease u set u.sortNoMax = ?2 where u = ?1")
    void updateSortNoMax(EntityIncrease t,int sortNoMax);
}
