package com.example.spring_food.controller;

import com.example.spring_food.Filter.SensitiveFilter;
import com.example.spring_food.aop.Limit.annotation.RedisLimit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController         //注解可以使结果以Json字符串的形式返回给客户端
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello SpringBoot";
    }
    @GetMapping("/cs")
    public Map<String, Object> cs() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "helloworld");
        return map;
    }


    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    /**
     * 测试接口限流
     * */
    @Test
    @GetMapping("/limit")
    @RedisLimit(key = "test",period = 100, count = 5,msg = "访问过于频繁，请重试！")
    public Map<String, Object> limit() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "接口限流"+ ATOMIC_INTEGER);
        return map;
    }

    @Autowired
    private SensitiveFilter sensitiveFilter;
    /**
     * 测试过滤敏感词
     * */
    @GetMapping("/sensitive")
    public Map<String, Object> sensitive(@RequestParam("word") String word) {
        Map<String, Object> map = new HashMap<>();
        String text = sensitiveFilter.filter(word);
        map.put("msg", text);
        return map;
    }

}
