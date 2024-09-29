package org.javaboy.api_sign_demo.config;

import org.javaboy.api_sign_demo.interceptor.SignInterceptor;
import org.javaboy.api_sign_demo.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    AppService appService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignInterceptor(appService))
                //只拦截需要接口验签的请求
                .addPathPatterns("/app/**");
    }
}
