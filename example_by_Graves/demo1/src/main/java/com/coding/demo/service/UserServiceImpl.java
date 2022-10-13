package com.coding.demo.service;

import com.coding.demo.mapper.UserMapper;
import com.coding.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User selectUser(String id){return userMapper.selectUser(id);}

    @Override
    public int insertUser(String name,String password) {
        return userMapper.saveUser(name,password);
    }

    @Override
    public int updateUser(String name, String email, String phone) {
        return userMapper.updateUser(name, email, phone);
    }

    @Override
    public int updatePassword(String name, String password) {
        return userMapper.updatePassword(name, password);
    }

    @Override
    public String findPassword(String name) {
        return userMapper.findPassword(name);
    }
}
