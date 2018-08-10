package com.zxsoft.fanfanfamily.config.converter;

import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FanFResponseBodyBuilder {
    public static <T> FanfAppData<T> ok(String msg, T t) {
        FanfAppData body = new FanfAppData();
        body.setData(t);
        body.setMsg(msg);
        body.setStatus(1);
        return body;
    }

    public static <T> FanfAppData<T> error(String msg, T t) {
        FanfAppData body = new FanfAppData();
        body.setData(t);
        body.setMsg(msg);
        body.setStatus(0);
        return body;
    }

    public static <T> FanfAppData<T> timeout(String msg, T t) {
        FanfAppData body = new FanfAppData();
        body.setData(t);
        body.setMsg(msg);
        body.setStatus(-1);
        return body;
    }

}
