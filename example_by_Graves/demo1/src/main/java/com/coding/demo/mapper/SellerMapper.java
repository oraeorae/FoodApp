package com.coding.demo.mapper;

import com.coding.demo.model.Seller;
import com.coding.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SellerMapper {
    @Insert("INSERT INTO seller(name,IdCard,introduction,location) VALUES(#{name},#{IdCard},#{introduction},#{location})")
    int saveSeller(String  name,String IdCard,String introduction,String location);

    @Select("select * from seller where name=#{name}")
    Seller selectSeller(@Param("name")String name);
}
