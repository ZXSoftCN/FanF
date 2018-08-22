package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.mort.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyTypeDao extends JpaRepository<PolicyType,String> {
    List<PolicyType> findAllByIdIsNotNullOrderBySortNo();
}
