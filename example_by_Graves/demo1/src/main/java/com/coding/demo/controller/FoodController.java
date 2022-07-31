package com.coding.demo.controller;

import com.coding.demo.model.Food;
import com.coding.demo.service.FoodServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/food")
@RestController
@ResponseBody
@Slf4j
public class FoodController {
    @Autowired
    private FoodServiceImpl foodService;

    @RequestMapping("/register")
    public String registerFood(String  name,String sellerID,String foodIntroduction,String species,String price){
        log.info("name:{}",name);
        log.info("sellerID:{}",sellerID);
        log.info("foodIntroduction:{}",foodIntroduction);
        log.info("species:{}",species);
        log.info("price:{}",price);
        if(StringUtils.isEmpty(name)){
            return "食物名称不能为空";
        }
        if(StringUtils.isEmpty(foodIntroduction)){
            return "食物介绍不能为空";
        }
        if(StringUtils.isEmpty(species)){
            return "种类不能为空";
        }
        if(StringUtils.isEmpty(price)){
            return "价格不能为空";
        }
        Food food=foodService.selectFood(name);
        if(food!=null){
            return "食物注册失败，该食物名已经存在";
        }
        int count=foodService.InsertFood(name, sellerID, foodIntroduction, species, price);
        if(count==0)return  "添加食物失败";
        return "添加食物成功";
    }
}
