package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

    //根据所属区域查询
    Page<Bank> findAllByRegionsInOrderByCode(Region region,Pageable pageable);
    List<Bank> findAllByRegionsInOrderByCode(Region region);
}
