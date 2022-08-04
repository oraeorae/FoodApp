package com.example.spring_food.dao;


import com.example.spring_food.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
//create table users(id int not null AUTO_INCREMENT,username varchar(100),password varchar(100),email varchar(50),type varchar(25) ,primary key (id));
public interface UserMapper {
    /**
     * 判断用户是否存在
     */
    @Select("SELECT count(*) FROM users where username=#{username}")
    int isUser(String username);

    /**
     * 判断用户名及密码是否正确
     */
    @Select("select count(*) from users WHERE username=#{username} and password=#{password}")
    int judgeUser(String username,String password);

    /**
     * 添加新的用户信息/注册
     */
    @Insert("insert into users(username,password,email) values(#{username},#{password},#{email})")
    int insertUser(User user);

    /**
     * 返回用户信息
     */
    @Select("select * from users WHERE username=#{username}")
    User getUser(String username);



}