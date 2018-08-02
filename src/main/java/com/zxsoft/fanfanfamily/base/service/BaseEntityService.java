package com.zxsoft.fanfanfamily.base.service;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;

public interface BaseEntityService<T extends BaseEntity> extends BaseService<T> {
    void loadCreator(T entity);//在服务层获取操作用户加载到创建人上
    void loadLastUpdater(T entity);//在服务层获取操作用户加载到最新更新人上
}
