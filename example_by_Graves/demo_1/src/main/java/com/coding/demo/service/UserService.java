package com.coding.demo.service;

import com.coding.demo.model.User;

public interface UserService {
    /**
     * 根据学生id查询用户
     * @param id
     * @return user
     */
    User selectUserById(String id);

    /**
     * 根据学生name查询用户
     * @param name
     * @return user
     */
    User selectUserByName(String name);

    /**
     * 给学生姓名和密码，插入新用户
     * @param name
     * @param password
     * @return int
     */
    int insertUser(String name,String password);

    /**
     * 当注册成功后，到用户界面添加新信息
     * @param name
     * @param email
     * @param phone
     * @return int
     */
    int updateUser(String name,String email,String phone);

    /**
     * 修改密码
     * @param name
     * @param password
     * @return int
     */
    int updatePassword(String name,String password);

    /**
     * 登录验证
     * @param name
     * @return
     */
    String findPassword(String name);
}
