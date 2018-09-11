package com.zxsoft.fanfanfamily.config.filter;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseEntity;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        headers.setAccessControlAllowCredentials(true);
        List<String> lstAll = new ArrayList<>();
        lstAll.add("*");
        headers.setAccessControlAllowHeaders(lstAll);
        List<HttpMethod> lstMethod = new ArrayList<>();
        lstMethod.add(HttpMethod.HEAD);
        lstMethod.add(HttpMethod.OPTIONS);
        lstMethod.add(HttpMethod.GET);
        lstMethod.add(HttpMethod.POST);
        lstMethod.add(HttpMethod.PATCH);
        headers.setAccessControlAllowMethods(lstMethod);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);


        String authToken = "";
        //&& ((FanfAppData) o).getStatus() > 0
        //FanfAppData中的Status是业务处理状态描述，token是系统机制不应受影响。
        if ((o.getClass() == FanfAppData.class )) {
            try{
                Subject current = SecurityUtils.getSubject();
                //TODO 将返回的Object JWT化绑定到token.（暂用Session记录）
                // SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal()
                if (current != null && current.getSession() != null) {
                    if (current.getSession().getAttributeKeys().contains("token")) {
                        authToken = current.getSession().getAttribute("token").toString();
                    }
                } else if (session != null) {
                    authToken = session.getAttribute("token").toString();
                }
            }catch (Exception ex) {
                //HttpSession获取不存在的key对象时，直接抛出异常。
                //无法提前检查
                authToken = "";
            }
            if (!authToken.isEmpty()) {
                if (headers.containsKey("token")) {
                    headers.set("token", authToken);
                } else {
                    headers.add("token",authToken);
                }
            }
        }
        super.addDefaultHeaders(headers, o, contentType);
    }
}
