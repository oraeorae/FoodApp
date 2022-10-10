package com.example.spring_food.dao;

import com.example.spring_food.pojo.User;
import com.example.spring_food.pojo.UserActiveDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author czh
 */
//create table user_recommend(id int not null AUTO_INCREMENT,userId int,businessId int,hits int,primary key (id))
@Mapper
public interface RecommendMapper {

    /**
     * 添加新的用户和商店信息
     */
    @Insert("insert into user_recommend(userId,businessId,hits) values(#{userId},#{businessId},1)")
    int insertUserActive(int userId,int businessId);

    /**
     * 更新新的用户和商店信息
     */
    @Update("update user_recommend set hits = hits+1 where userId=#{userId} and businessId=#{businessId}")
    int updateUserActive(int userId,int businessId);


    /**
     * 判断是否有该用户和商店信息
     */
    @Select("select count(*) from user_recommend where userId=#{userId} and businessId=#{businessId}")
    int isUserActive(int userId,int businessId);

    /**
     * 返回用户和商店信息
     */
    @Select("select userId,businessId,hits from user_recommend")
    List<UserActiveDTO> listUserActive();

    /**
     * 返回指定的用户  及商店信息
     */
    @Select("select userId,businessId,hits from user_recommend where userId=#{userId}")
    UserActiveDTO getUserActive(int userId);

}
