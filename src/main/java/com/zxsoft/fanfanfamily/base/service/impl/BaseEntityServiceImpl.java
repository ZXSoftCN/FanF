package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.service.BaseEntityService;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated
public abstract class BaseEntityServiceImpl<T extends BaseEntity> extends BaseServiceImpl<T>
                implements BaseEntityService<T>{
    //根据Session或Token方式加载entity的创建人或最新修改人
    @Override
    public void loadCreator(T entity) {

    }

    //根据Session或Token方式加载entity的最新修改人
    @Override
    public void loadLastUpdater(T entity) {

    }
}
