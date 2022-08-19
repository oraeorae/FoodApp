package com.coding.demo.service;

import com.coding.demo.model.Food;
import com.coding.demo.model.Seller;

import java.util.List;

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

    /**
     * 更新食物照片
     * @param pictureUrl
     * @param id
     * @return
     */
    int  updateFoodImg(String pictureUrl, Integer id);

    /**
     * 分页式管理
     * @param page
     * @param limit
     * @return
     */
    List<Food> pageFood(int page, int limit);

    /**
     * 查找对应的食物
     * @param find
     * @param page
     * @param limit
     * @return
     */
    List<Food> searchFood(String find,int page, int limit);

    /**
     * 删除食物
     * @param name
     */
    void deleteFood(String name);
}
