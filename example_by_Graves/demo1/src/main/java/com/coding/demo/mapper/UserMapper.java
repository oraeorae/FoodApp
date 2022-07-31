package com.coding.demo.mapper;

import com.coding.demo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {


    @Insert("INSERT INTO user(name,password) VALUES(#{name},#{password}) ")
    int saveUser(@Param("name") String name, @Param("password") String password);


    @Select("select * from user where name=#{name}")
    User selectUser(@Param("name")String name);

    @Update("UPDATE user set email=#{email},phone=#{phone} WHERE name =#{name}")
    int updateUser(@Param("name")String name,@Param("email")String email,@Param("phone")String phone);

    @Update("UPDATE `user`   set `password`=#{password}  WHERE `name`=#{name}")
    int updatePassword(@Param("name")String name,@Param("password")String password);
}
