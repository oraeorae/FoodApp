package com.coding.demo.service;

import com.coding.demo.mapper.FoodMapper;
import com.coding.demo.model.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl implements FoodService{
    @Autowired
    private FoodMapper foodMapper;
    @Override
    public int InsertFood(String name, String sellerID, String foodIntroduction, String species, String price) {
        return foodMapper.saveFood(name, sellerID, foodIntroduction, species, price);
    }

    @Override
    public Food selectFood(String name) {
        return foodMapper.selectFood(name);
    }
}
