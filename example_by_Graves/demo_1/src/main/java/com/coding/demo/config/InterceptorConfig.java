package com.coding.demo.config;

import com.coding.demo.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
@Configuration//定义此类为配置类
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String[] addPathPatterns={

        };
        String[] excludePathPatterns={

        };
        registry.addInterceptor(new UserInterceptor()).addPathPatterns().excludePathPatterns();
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
*/