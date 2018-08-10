package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.annotation.JSONField;

public class FanfAppData<T> {
    String msg = "";
    Integer status = 1;
    T data = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JSONField(name = "data")
    public T getData() {
        return data;
    }

    @JSONField(name = "data")
    public void setData(T data) {
        this.data = data;
    }
}
