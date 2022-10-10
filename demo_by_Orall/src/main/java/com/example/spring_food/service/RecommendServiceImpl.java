package com.example.spring_food.service;

import com.example.spring_food.dao.RecommendMapper;
import com.example.spring_food.pojo.UserActiveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    RecommendMapper recommendMapper;


    @Override
    public int insertUserActive(int userId, int businessId) {
        return recommendMapper.insertUserActive(userId,businessId);
    }

    @Override
    public int updateUserActive(int userId, int businessId) {
        return recommendMapper.updateUserActive(userId,businessId);
    }

    @Override
    public int isUserActive(int userId, int businessId) {
        return recommendMapper.isUserActive(userId,businessId);
    }

    @Override
    public List<UserActiveDTO> listUserActive() {
        return recommendMapper.listUserActive();
    }

    @Override
    public UserActiveDTO getUserActive(int userId) {
        return null;
    }
}