package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseEntityDao extends JpaRepository<BaseEntity,String> {
    @Override
    Optional<BaseEntity> findById(String s);

}
