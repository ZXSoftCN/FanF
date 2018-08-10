package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.annotation.JSONField;
import io.micrometer.core.lang.Nullable;

//POJO
//用于响应前端数据格式
public class FanFResponseEntity<T>{
//    如果请求没有任何问题，那status返回值是1；
//    如果请求错误，比如说参数错误或者其他报错之类的，那status返回值就是0；
//    如果status值是-1，表示登录超时，那么就会跳出登录。
    private int Status;
    private String msg;//响应消息内容
    private T t;//包含对象，json名：data.

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Nullable
    @JSONField(name = "data")
    public T getT() {
        return t;
    }
    @JSONField(name = "data")
    public void setT(T t) {
        this.t = t;
    }

    public static <T> FanFResponseEntity<T> ok(String msg, T t) {
        FanFResponseEntity<T> item = new FanFResponseEntity<>();
        item.setStatus(1);
        item.setMsg(msg);
        item.setT(t);

        return item;
    }

    public static <T> FanFResponseEntity<T> error(String msg, T t) {
        FanFResponseEntity<T> item = new FanFResponseEntity<>();
        item.setStatus(0);
        item.setMsg(msg);
        item.setT(t);

        return item;
    }

    public static <T> FanFResponseEntity<T> timeout(String msg, T t) {
        FanFResponseEntity<T> item = new FanFResponseEntity<>();
        item.setStatus(-1);
        item.setMsg(msg);
        item.setT(t);

        return item;
    }
}
