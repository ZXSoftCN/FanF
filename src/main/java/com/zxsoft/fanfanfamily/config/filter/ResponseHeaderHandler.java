package com.zxsoft.fanfanfamily.config.filter;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
* 扩展FastJsonHttpMessageConverter，加入Authorization Token进入Header中。
 */
public class ResponseHeaderHandler extends FastJsonHttpMessageConverter {


    @Autowired
    private HttpSession session;

    /*
        在JWT中
     */
    @Override
    protected void addDefaultHeaders(HttpHeaders headers, Object o, MediaType contentType) throws IOException {

        String authToken = "";
        Subject current = SecurityUtils.getSubject();
        if (current != null && current.getSession() != null) {
            if (current.getSession().getAttributeKeys().contains("token")) {
                authToken = current.getSession().getAttribute("token").toString();
            }
        } else if (session != null) {
            try {
                authToken = session.getAttribute("token").toString();
            } catch (Exception ex) {
                //HttpSession获取不存在的key对象时，直接抛出异常。
                //无法提前检查
                authToken = "";
            }
        }
        if (!authToken.isEmpty()) {
            if (headers.containsKey("token")) {
                headers.set("token", authToken);
            } else {
                headers.add("token",authToken);
            }
        }
        super.addDefaultHeaders(headers, o, contentType);
    }
}
