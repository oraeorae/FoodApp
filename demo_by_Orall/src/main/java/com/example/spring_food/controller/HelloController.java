package com.example.spring_food.controller;

import com.example.spring_food.utils.SensitiveFilter;
import com.example.spring_food.aop.Limit.annotation.RedisLimit;
import com.example.spring_food.pojo.User;
import com.example.spring_food.service.LoginService;
import com.example.spring_food.utils.StatusCode;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController         //注解可以使结果以Json字符串的形式返回给客户端
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "hello SpringBoot";
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/cs")
    public Map<String, Object> cs() {
        System.out.println("正在发送队列");
        rabbitTemplate.convertAndSend("mysql"," context");
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "helloworld");
        return map;
    }


    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
    /**
     * 测试接口限流
     * */
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
    @Test
    @GetMapping("/sensitive")
    public Map<String, Object> sensitive(@RequestParam("word") String word) {
        Map<String, Object> map = new HashMap<>();
        String text = sensitiveFilter.filter(word);
        map.put("msg", text);
        return map;
    }

    /**
     * 测试登录
     * */

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Map<String, Object> login(@Valid User user){
        try{
            Map<String, String> tmp = new HashMap<>();
            tmp.put("token",loginService.login(user));
            return StatusCode.success(tmp);
        }catch (Exception e){
            e.printStackTrace();
            return StatusCode.error(5001,e.getMessage());
        }
    }



    @GetMapping("/rabbitmq")
    public Map<String, Object> testSend() {
        // 发出一条消息
        System.out.println("正在发送信息");
        //rabbitTemplate.convertAndSend("mysql"," context");
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "helloworld");
        return map;
    }
}
