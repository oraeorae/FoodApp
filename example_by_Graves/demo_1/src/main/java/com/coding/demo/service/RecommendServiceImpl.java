package com.coding.demo.service;

import com.coding.demo.mapper.RecommendMapper;
import com.coding.demo.model.UserActiveDTO;
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