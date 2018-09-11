package com.zxsoft.fanfanfamily.base.service;

import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EntityIncreaseService extends BaseService<EntityIncrease> {

    Optional<EntityIncrease> findByEntityName(String entityName);

    int getSortNoMaxPlus(String entityName);
    int getSortNoMaxPlus(EntityIncrease t);

    Page<EntityIncrease> findEntityIncreaseByName(String name, Pageable pageable);
}
