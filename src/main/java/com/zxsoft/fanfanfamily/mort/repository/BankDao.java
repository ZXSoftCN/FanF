package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankDao extends JpaRepository<Bank,String> {

    Bank findByCodeIgnoreCase(String code);

    //一组惯用查询方法
    Optional<Bank> findFirstByCodeIgnoreCase(String code);
    Optional<Bank> findFirstByCode(String code);
    Optional<Bank> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Bank> findFirstByNameContaining(String namePart);
    Optional<Bank> findFirstByCodeOrName(String code,String name);
    Page<Bank> findByNameContaining(String nameLike, Pageable page);
    List<Bank> findAllByNameContaining(String nameLike);
    Page<Bank> findByCodeStartingWith(String codeStart,Pageable page);
    List<Bank> findAllByCodeStartingWith(String codeStart);
    List<Bank> findAllByParentBankIsNullOrderByCode();//查顶级银行
    List<Bank> findAllByIconUrlContaining(String iconUrl);//查询共用图标路径的项
    List<Bank> findAllByIconUrlContainingAndIdNot(String iconUrl,String id);//查询其他共用图标项
    //扩展
    @EntityGraph(attributePaths = { "regions"})
    Optional<Bank> queryFirstByCode(String code);
    @EntityGraph(attributePaths = { "regions"})
    Optional<Bank> queryFirstByNameContaining(String nameLike);
    @EntityGraph(attributePaths = { "regions"})
    Optional<Bank> queryBankById(String id);

    //根据所属区域查询
    Page<Bank> findAllByRegionsInOrderByCode(Region region,Pageable pageable);
    List<Bank> findAllByRegionsInOrderByCode(Region region);

    @EntityGraph(attributePaths = { "parentBank","regions"})
    @Query(value = "select u from Bank u where u.parentBank.id = ?1 order by u.code")
    List<Bank> customQueryAllByParentBankId(String id);//查下级银行
}
