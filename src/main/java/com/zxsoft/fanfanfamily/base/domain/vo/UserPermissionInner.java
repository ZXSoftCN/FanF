package com.zxsoft.fanfanfamily.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.zxsoft.fanfanfamily.base.domain.Permission;

import java.util.ArrayList;
import java.util.List;

public class UserPermissionInner {
    private String id;
    private String name;
    private String userName;
    private boolean state;

    private List<Permission> permissions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
