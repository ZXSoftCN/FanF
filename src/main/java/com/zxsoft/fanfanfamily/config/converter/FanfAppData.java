package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * //    如果请求没有任何问题，那status返回值是1；
 //    如果请求错误，比如说参数错误或者其他报错之类的，那status返回值就是0；
 //    如果status值是-1，表示登录超时，那么就会跳出登录。
 * @param <T>
 */
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
