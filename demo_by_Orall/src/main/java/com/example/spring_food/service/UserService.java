package com.example.spring_food.service;


import com.example.spring_food.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @author czh
 */
public interface UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    boolean login(User user);

}