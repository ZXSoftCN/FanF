package com.zxsoft.fanfanfamily.base.domain;

import javax.persistence.PostUpdate;

public interface EntityPersistExpand {
    String getEntityClassName();//获取实体对象类名
    void onSetDefault();//实体持久化save前，设定预设值
    void onPostPersist();//实体持久化Save后，扩展方法
    void onPreUpdate();
    void onPostUpdate();
}
