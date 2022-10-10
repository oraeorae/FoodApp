package com.example.spring_food.test;

import com.example.spring_food.dao.UserMapper;
import com.example.spring_food.pojo.User;
import com.example.spring_food.utils.RabbitmqListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 测试缓存同步
 * @author czh
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisCacheSyncTest {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void addCache(){
        //并放入redis缓存
        User user = new User();
        user.setEmail("1242@qq.163");
        user.setId(5);
        user.setName("166");
        user.setTele("1496");
        redisTemplate.opsForValue().set("user_"+user.getName(), user, 30, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("user_"+user.getName()));
    }


    @Test
    public void Cache(){
        redisTemplate.opsForValue().set("test", "123", 30, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("test"));
    }

    @Test
    public void select(){
        String name = "166";
        //为提升系统性能和用户体验
        //首先在Redis缓存中查询，如果有，直接使用；如果没有，去数据库查询并放入redis缓存
        User user = (User) redisTemplate.opsForValue().get("username_"+name);
        //System.out.println("缓存里的数据是："+user.toString());
        if (null == user) {
            System.out.println("缓存中没有");
            //去数据库查询
            user = userMapper.getUser(name);
            System.out.println("数据库查询结果："+user.toString());
        }
        //并放入redis缓存
        redisTemplate.opsForValue().set("username_"+name, user, 30, TimeUnit.SECONDS);
        user = (User) redisTemplate.opsForValue().get("username_"+name);
        System.out.println("缓存里的数据是："+user.toString());
    }
    @Autowired
    RabbitmqListener rabbitmqListener;

    @Test
    public void rabb(){
        //rabbitmqListener.businessQueue();
    }



}


