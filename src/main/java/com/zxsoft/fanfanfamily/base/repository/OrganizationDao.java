package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrganizationDao extends JpaRepository<Organization,String> {

    //一组惯用查询方法
    Optional<Organization> findFirstByCode(String code);
    Optional<Organization> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Organization> findFirstByNameContaining(String namePart);
    Optional<Organization> findFirstByCodeOrName(String code,String name);
    Page<Organization> findByNameContaining(String nameLike, Pageable page);
    List<Organization> findAllByNameContaining(String nameLike);
    Page<Organization> findByCodeStartingWith(String codeStart,Pageable page);
    List<Organization> findAllByCodeStartingWith(String codeStart);

    //扩展
    Page<Organization> findByParentOrg(Organization parentOrg,Pageable page);
    List<Organization> findAllByParentOrg(Organization parentOrg);

    //自定义查询
    @Query(value = "select u from Organization u where u.parentOrg.code = ?1 order by code")
    List<Organization> customQueryByParentOrgCode(String code);

    List<Organization> queryByParentOrgCode(String code);
    Page<Organization> queryByParentOrgCode(String code,Pageable page);
    List<Organization> queryByParentOrgNameContaining(String parentOrgNameLike);

    @Query(value = "select u from Organization u where u.code = ?1 order by u.code")
    Optional<Organization> customQueryByCode(String code);
    @Query(value = "select u from Organization u where ?1 between u.createTime and u.lastUpdateTime")
    List<Organization> customQueryByTestDate(Date date);//传入参数时会根据查询Entity的属性类型进行校验。这里须转化成java.util.Date

}
