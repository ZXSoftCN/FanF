package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseEntityDao extends JpaRepository<BaseEntity,String> {
}
