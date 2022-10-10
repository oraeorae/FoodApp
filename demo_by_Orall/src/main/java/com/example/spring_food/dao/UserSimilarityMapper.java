package com.example.spring_food.dao;

import com.example.spring_food.pojo.UserSimilarityDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author czh
 */
//create table user_similarity(id int not null AUTO_INCREMENT,userId int,userRefId int,similarity double,primary key (id))
@Mapper
public interface UserSimilarityMapper {
    /**
     * 新增用户相似度数据
     * @param userSimilarityDTO 用户相似度数据
     * @return 受影响到的行数，0表示影响0行，1表示影响1行
     */
    @Insert("INSERT INTO user_similarity(userId, userRefId, similarity) VALUES(#{userId}, #{userRefId}, #{similarity})")
    int saveUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 更新用户相似度数据
     * @param userSimilarityDTO 用户相似度数据
     * @return 受影响到的行数，0表示影响0行，1表示影响1行
     */
    @Update("UPDATE user_similarity SET similarity = #{similarity} WHERE userId = #{userId} AND userRefId = #{userRefId}")
    int updateUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 判断某两个用户之间的相似度是否已经存在
     * @param userSimilarityDTO 存储两个用户的id
     * @return 返回1表示已经存在，返回0表示不存在
     */
    @Select("SELECT COUNT(*) FROM user_similarity WHERE userId = #{userId} AND userRefId = #{userRefId} " +
            "OR userRefId = #{userId} AND userId = #{userRefId}")
    int countUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    /**
     * 查询某个用户与其他用户之间的相似度列表
     * @param userId 带查询的用户id
     * @return 该用户与其他用户的相似度列表
     */
    @Select("SELECT userId, userRefId, similarity FROM user_similarity WHERE userId = #{userId} OR userRefId = #{userId}")
    List<UserSimilarityDTO> listUserSimilarityByUId(Long userId);

}
