package com.zxsoft.fanfanfamily.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

//@Configuration
@Order(1)
@WebFilter(filterName = "CharacterEncodingFilter",urlPatterns = "/*",
        initParams = {
            @WebInitParam(name="forceEncoding",value = "true"),
            @WebInitParam(name="encoding",value = "utf-8")
        })
public class CharacterEncodingFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init-----------filter");
        config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean enabled = "true".equalsIgnoreCase(config.getInitParameter("forceEncoding").trim());//启用
        String encoding = config.getInitParameter("encoding");

        System.out.println("----------characterfilter");

        if(enabled || encoding != null){//如果启用该Filter
            servletRequest.setCharacterEncoding(encoding);//设置request编码
            servletResponse.setCharacterEncoding(encoding);//设置response编码
        }
        filterChain.doFilter(servletRequest,servletResponse );//doFilter,执行下一个Filter或者Servlet
    }

    @Override
    public void destroy() {
        System.out.println("destroy----------filter");
    }
}
