package com.coding.demo.mapper;

import com.coding.demo.model.Food;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FoodMapper {
    @Insert("INSERT INTO food(name,sellerID,foodIntroduction,species,price) VALUES(#{name},#{sellerID},#{foodIntroduction},#{species},#{price})")
    int saveFood(@Param("name") String  name, @Param("sellerID") String sellerID,
                 @Param("foodIntroduction") String foodIntroduction, @Param("species") String species, String price);

    @Select("SELECT * FROM food where name=#{name}")
    Food selectFood(@Param("name")String name);
}
