package com.zxsoft.fanfanfamily.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
public class FanfWebMvcConfig implements WebMvcConfigurer {


    @Autowired
    private AppPropertiesConfig appPropertiesConfig;

    /**
     * 添加标准化报文拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


//        registry.addInterceptor(new ResResultBindingInterceptor()).addPathPatterns("/**");

    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.extendMessageConverters(converters);
        //创建fastJson消息转换器
        HttpMessageConverter c = CustomFastJsonConverter();

        //将fastjson添加到视图消息转换器列表内
        converters.add(0,c);//设置fastjsonconverter转换器的序位
    }

    /**
     * 消息内容转换配置
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);

    }

    /**
     * 跨域CORS配置
     * 设定/api下url可以被配置文件中的各个域名跨域访问
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        super.addCorsMappings(registry);
        List<String> lstOrgCC = appPropertiesConfig.getCrossOriginLocations();
        String[] collCross = new String[]{};
        collCross = lstOrgCC.toArray(collCross);
        registry.addMapping("/api/**")
                .allowedHeaders("*")
                .allowedMethods("POST","GET")
                .allowedOrigins(collCross);
    }

    @Bean
    public HttpMessageConverter CustomFastJsonConverter(){
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();//2

        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();

        supportedMediaTypes.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
        supportedMediaTypes.add(new MediaType("text", "html", Charset.forName("UTF-8")));
        supportedMediaTypes.add(new MediaType("text", "json", Charset.forName("UTF-8")));
        supportedMediaTypes.add(new MediaType("application", "json", Charset.forName("UTF-8")));
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.PrettyFormat
        );
        fastJsonConfig.setDateFormat(appPropertiesConfig.getFastJsonDateFormat());
        fastConverter.setFastJsonConfig(fastJsonConfig);


        HttpMessageConverter<?> converter = fastConverter;

        return converter;
    }
}
