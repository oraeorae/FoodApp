package com.example.spring_food.service;

import com.example.spring_food.dao.UserMapper;
import com.example.spring_food.pojo.LoginUser;
import com.example.spring_food.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息
        //没有查询到，则抛出异常
        if( userMapper.isUser(username) == 0 ){
            throw new RuntimeException("用户名或者密码错误");
        }
        User user = userMapper.getUser(username);
        System.out.println("asa");
        //TODO 查询对应的权限信息

        //把数据封装成UserDeatails返回
        return new LoginUser(user);
    }
}
