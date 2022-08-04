package com.example.spring_food.service;

import com.example.spring_food.dao.UserMapper;
import com.example.spring_food.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 注册
     * */
    @Override
    public boolean register(User user){
        //如果不存在该用户
        if( userMapper.isUser(user.getUsername()) == 0 ) {
            userMapper.insertUser(user);
            return true;
        }else{

            return false;
        }
    }

    /**
     * 登录
     * */
    @Override
    public boolean login(User user){

       if( userMapper.judgeUser(user.getUsername(),user.getPassword()) != 0 ){
            return true;
       }else{
           return false;
       }
    }
}