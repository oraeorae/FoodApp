package com.example.spring_food.service;

import com.example.spring_food.pojo.User;
import org.springframework.stereotype.Service;


public interface LoginService {
    public String login(User user);
}
