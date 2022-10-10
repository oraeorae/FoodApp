package com.example.spring_food.service;


import com.example.spring_food.pojo.UserActiveDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author czh
 */
public interface RecommendService {
    /**
     * 添加新的用户和商店信息
     */
    int insertUserActive(int userId,int businessId);

    /**
     * 更新新的用户和商店信息
     */
    int updateUserActive(int userId,int businessId);

    /**
     * 判断是否有该用户和商店信息
     */
    int isUserActive(int userId,int businessId);

    /**
     * 返回用户和商店信息
     */
    List<UserActiveDTO> listUserActive();

    /**
     * 返回指定的用户  及商店信息
     */
    UserActiveDTO getUserActive(int userId);

}