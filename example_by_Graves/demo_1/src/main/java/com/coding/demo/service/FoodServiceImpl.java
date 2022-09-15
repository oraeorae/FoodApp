package com.coding.demo.service;

import com.coding.demo.mapper.FoodMapper;
import com.coding.demo.model.Food;
import com.coding.demo.model.Seller;
import com.coding.demo.utils.SensitiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FoodServiceImpl implements FoodService{
    @Autowired
    private FoodMapper foodMapper;
    private RedisTemplate redisTemplate;
    private SensitiveUtils sensitiveUtils;


    @Override
    public int InsertFood(String name, String sellerID, String foodIntroduction, String species, String price) {
        //2022.8.24 新增或修改部分   by:Orall（功能：敏感词过滤）
        return foodMapper.saveFood(sensitiveUtils.filter(name), sellerID, sensitiveUtils.filter(foodIntroduction), species, price);
    }

    @Override
    public Food selectFood(String name) {
        return foodMapper.selectFood(name);
    }

    @Override
    public int updateFoodImg(String pictureUrl, Integer id) {
        return  foodMapper.updateFoodImg(pictureUrl, id);
    }

    @Override
    public List<Food> pageFood(int page, int limit,int sellerID) {
        //为提升系统性能和用户体验
        //首先在Redis缓存中查询，如果有，直接使用；如果没有，去数据库查询并放入redis缓存
        List<Food>  listFood = (List<Food>) redisTemplate.opsForValue().get("listFood_"+page);
        if (null == listFood) {
            //去数据库查询
            //{(page -1) * limit},#{limit};")
            int first = (page - 1) * limit;
            int second = limit;
            listFood = foodMapper.pageFood(first, second,sellerID);
            //只返回第一张图片
            for( Food t : listFood ){
                String picture = t.getPictureUrl();
                int f = picture.indexOf("|");
                if( f != -1 ) {
                    t.setPictureUrl(picture.substring(0, f));
                }
            }
            //并放入redis缓存
            redisTemplate.opsForValue().set("listFood_"+page, listFood, 30, TimeUnit.SECONDS);
        }

        return listFood;
    }

    @Override
    public List<Food> searchFood(String find, int page, int limit) {
        //为提升系统性能和用户体验
        //首先在Redis缓存中查询，如果有，直接使用；如果没有，去数据库查询并放入redis缓存
        List<Food>  listFood = (List<Food>) redisTemplate.opsForValue().get("listFood_"+page);
        if (null == listFood) {
            //去数据库查询
            //{(page -1) * limit},#{limit};")
            int first = (page - 1) * limit;
            int second = limit;
            listFood = foodMapper.searchFood(find,first, second);
            //只返回第一张图片
            for( Food t : listFood ){
                String picture = t.getPictureUrl();
                int f = picture.indexOf("|");
                if( f != -1 ) {
                    t.setPictureUrl(picture.substring(0, f));
                }
            }
            //并放入redis缓存
            redisTemplate.opsForValue().set("listFood_"+page, listFood, 30, TimeUnit.SECONDS);
        }

        return listFood;
    }

    @Override
    public void deleteFood(String name) {
        foodMapper.deleteFood(name);
    }
}
