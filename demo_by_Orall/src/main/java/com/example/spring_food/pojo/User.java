package com.example.spring_food.pojo;

import lombok.Data;

import java.io.Serializable;

//create table users(id int not null AUTO_INCREMENT,username varchar(100),password varchar(100),email varchar(50),type varchar(25) ,primary key (id));
/**
 * 用户实体类
 * name 昵称
 * email 邮箱
 * username 用户名
 * password 密码
 * tele 联系方式
 * type 类型
 * 头像   后期补上
 * @author czh
 */
@Data       //使用这个注解可以省去代码中大量的get()、 set()、 toString()等方法；
public class User implements Serializable{
    int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String tele;
    private String type = "用户";
}