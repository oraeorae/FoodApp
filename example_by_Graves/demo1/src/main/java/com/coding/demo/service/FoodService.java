package com.coding.demo.service;

import com.coding.demo.model.Food;

public interface FoodService {
    /**
     * 添加新的食物
     * @param name
     * @param sellerID
     * @param foodIntroduction
     * @param species
     * @param price
     * @return int
     */
    int InsertFood(String  name,String sellerID,String foodIntroduction,String species,String price);

    /**
     * 查询表中名为name的食物
     * @param name
     * @return Food
     */
    Food selectFood(String name);
}
