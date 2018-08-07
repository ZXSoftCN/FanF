package com.zxsoft.fanfanfamily.config;

import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.beans.Transient;
import java.io.IOException;

/*
*   OpenSessionFilter用于使用过滤器将请求的全部 hibernate session 绑定到线程。 用于“打开回话视图” 模式，
　　　　允许在web 视图中延迟加载，尽管基本的事务已经完成，（虽然事务已经提交完成，但任然允许延迟加载数据？）
*   这个过滤器使hibernaete session 可以通过当前线程，其将自动检测事务管理，
*   适合于服务处通过HibernateTransactionManager 管理事务，以及非事务执行（如果适当配置）
*
*   OpenSessionFilter中要指定SessionFactoryBeanName。当前系统使用shiro中Session进行管理，所以还不知道
*   如何处理加入到OpenSessionFilter中。
*   【未使用】
 */

//@Order(2)
//@WebFilter(filterName = "OpenSessionFilter",urlPatterns = "/*",
//        initParams = {
//                @WebInitParam(name="singleSession",value = "true")
//        })
//public class OpenSessionFilter implements Filter {
//
//    private final OpenSessionInViewFilter filter;
//
//    public OpenSessionFilter(){
//        filter = new OpenSessionInViewFilter();
//        filter.setSessionFactoryBeanName("sessionFactory");//需要指定SessionFactory的BeanName
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        boolean enabled = "true".equalsIgnoreCase(filterConfig.getInitParameter("singleSession").trim());//启用
//        filter.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        filter.doFilter(servletRequest,servletResponse,filterChain);
//    }
//
//    @Override
//    public void destroy() {
//        filter.destroy();
//    }
//}
