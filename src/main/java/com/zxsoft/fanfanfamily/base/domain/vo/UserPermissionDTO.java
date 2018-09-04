package com.zxsoft.fanfanfamily.base.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class UserPermissionDTO {
    private UserPermissionInner inner = null;

    @JSONField(name = "user")
    public UserPermissionInner getInner() {
        return inner;
    }

    public void setInner(UserPermissionInner inner) {
        this.inner = inner;
    }
}
