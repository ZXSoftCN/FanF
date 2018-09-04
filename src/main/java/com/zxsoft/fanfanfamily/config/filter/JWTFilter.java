package com.zxsoft.fanfanfamily.config.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.base.service.impl.UserInfoServiceImpl;
import com.zxsoft.fanfanfamily.common.JWTUtil;
import com.zxsoft.fanfanfamily.config.JWTToken;
import com.zxsoft.fanfanfamily.config.converter.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含token字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("token");
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        if (httpRequest.getHeader("token") == null || httpRequest.getHeader("token").isEmpty()) {
            throw new AuthenticationException("Header未能提供token");
        }
        String authorization = httpRequest.getHeader("token");

        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 【原版本】
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    /**
     * 改为认证失败后返回false，转至onAccesssDenied处理。
     * 这样不会进入业务服务中Controller。
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            }  catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /*
        isAccessAllowed返回false执行该方法。
        父类方法，会再次调用executeLogin。如果false，则直接返回异常信息，不再进行跳转。
        override：再次调用executeLogin，异常后跳转不直接返回异常消息，返回false。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean bIsLogin = false;
        try {
            bIsLogin =executeLogin(request, response);
        }catch (AuthenticationException e) {
            sendDeniedMsg(request,response,e.getMessage());//Realm认证只返回默认消息，无法定位到认证超时、无效的明细信息。

        } catch (Exception e) {
            sendDeniedMsg(request,response,e.getMessage());
        }
        return bIsLogin;
    }

    private void sendDeniedMsg(ServletRequest request, ServletResponse response,String msg){
        FanFResponseEntity entity = new FanFResponseEntity();
        entity.setStatus(0);
        entity.setMsg(String.format("认证失败：%s",msg));
        entity.setT(new Object());
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");

        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        if (httpRequest.getHeader("token") != null && !httpRequest.getHeader("token").isEmpty()) {
            httpResponse.setHeader("token",httpRequest.getHeader("token"));
        }
        try {
            PrintWriter out = httpResponse.getWriter();
            JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
            SerializerFeature[] serializerFeatures = {
//                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse,
                    SerializerFeature.WriteEnumUsingToString,
//                    SerializerFeature.WriteNullListAsEmpty,
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.PrettyFormat};

            NoLazyPropertyFilter filter = new NoLazyPropertyFilter();
            String strObj = JSON.toJSONString(entity,filter,serializerFeatures);
            out.append(strObj);
        } catch (IOException exception) {
            httpResponse.setStatus(401);
            String authcHeader = this.getAuthcScheme() + " realm=\"" + this.getApplicationName() + "\"";
            httpResponse.setHeader("WWW-Authenticate", authcHeader);
        }

    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;//option请求，有Response设置OK后。返回false，不再进行后续handlechain,直接返回
        }
        return super.preHandle(request, response);
    }

    /**
     * 将非法请求跳转到 /401
     */
    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 将认证失败跳转到 /403
     */
    private void response403(ServletRequest req, ServletResponse resp) {
        PrintWriter out = null ;
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;

        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try {
//            httpServletResponse.sendRedirect("/403");
            JSONObject res = new JSONObject();
            res.put("success", "false");
            res.put("msg", "认证失败");
            out = httpServletResponse.getWriter();
            out.append(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new AuthenticationException("------认证错误：Unauthorized-------");
        }
    }
}
