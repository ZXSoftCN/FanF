package com.zxsoft.fanfanfamily.base.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class UserInfoDTO {
    private String userName;
    private String name;
    private String password;
    private String token;

    @JSONField(alternateNames = {"ticket","userName","username"})
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(serialize = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
