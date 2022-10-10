package com.example.spring_food.service;

import com.example.spring_food.dao.RecommendMapper;
import com.example.spring_food.dao.UserSimilarityMapper;
import com.example.spring_food.pojo.UserActiveDTO;
import com.example.spring_food.pojo.UserSimilarityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSimilarityServiceImpl implements UserSimilarityService {
    @Autowired
    UserSimilarityMapper userSimilarityMapper;


    @Override
    public int saveUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return userSimilarityMapper.saveUserSimilarity(userSimilarityDTO);
    }

    @Override
    public int updateUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return userSimilarityMapper.updateUserSimilarity(userSimilarityDTO);
    }

    @Override
    public int isExistsUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return userSimilarityMapper.countUserSimilarity(userSimilarityDTO);
    }

    @Override
    public List<UserSimilarityDTO> listUserSimilarityByUId(Long userId) {
        return userSimilarityMapper.listUserSimilarityByUId(userId);
    }
}