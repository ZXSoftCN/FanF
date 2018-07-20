package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankDao extends JpaRepository<Bank,String> {

    Bank findByBankCodeIgnoreCase(String code);

    //根据所属区域查询
    List<Bank> findBanksByRegion(Region regionId);
    Page<Bank> queryBanksByRegion(Region regionId, Pageable pageable);

    List<Bank> findBanksByRegion_Code(String regionCode);
    List<Bank> findBanksByRegion_Id(String regionId);

    Page<Bank> queryBanksByRegion_Id(String regionId, Pageable pageable);
    Page<Bank> queryBankByRegion_Code(String regionCode, Pageable pageable);
}
