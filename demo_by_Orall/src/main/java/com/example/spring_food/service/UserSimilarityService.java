package com.example.spring_food.service;


import com.example.spring_food.pojo.UserActiveDTO;
import com.example.spring_food.pojo.UserSimilarityDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author czh
 */
public interface UserSimilarityService {
    /**
     * 新增用户相似度数据
     * @param userSimilarityDTO 用户相似度数据
     * @return 受影响到的行数，0表示影响0行，1表示影响1行
     */
    int saveUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 更新用户相似度数据
     * @param userSimilarityDTO 用户相似度数据
     * @return 受影响到的行数，0表示影响0行，1表示影响1行
     */
    int updateUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 判断某两个用户之间的相似度是否已经存在
     * @param userSimilarityDTO 存储两个用户的id
     * @return 返回1表示已经存在，返回0表示不存在
     */
    @Select("SELECT COUNT(*) FROM user_similarity WHERE user_id = #{userId} AND userRefId = #{userRefId} " +
            "OR userRefId = #{userId} AND user_id = #{userRefId}")
    int isExistsUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 查询某个用户与其他用户之间的相似度列表
     * @param userId 带查询的用户id
     * @return 该用户与其他用户的相似度列表
     */
    List<UserSimilarityDTO> listUserSimilarityByUId(Long userId);
}