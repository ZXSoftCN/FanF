package com.zxsoft.fanfanfamily.base.domain.vo;

import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;

import javax.persistence.NamedNativeQuery;
import java.util.List;

public class UserPermissionNativeDTO {
    private String id;
    private String name;
    private String userName;
    private boolean state;
    private String pName;
    private String resourceType;
    private String url;

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

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
