package com.coding.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.redis.core.RedisTemplate;

import javax.mail.MessagingException;

@SpringBootApplication
@MapperScan("com.coding.demo.mapper")
public class Demo1Application  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        //参数为当前springboot启动类
        //构建新资源
        return builder.sources(Demo1Application.class);
    }

}
