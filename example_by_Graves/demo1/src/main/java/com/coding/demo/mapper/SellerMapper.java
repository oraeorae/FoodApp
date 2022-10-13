package com.coding.demo.mapper;

import com.coding.demo.model.Seller;
import com.coding.demo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SellerMapper {
    /**
     *添加新的商家
     */
    @Insert("INSERT INTO seller(name,IdCard,introduction,location) VALUES(#{name},#{IdCard},#{introduction},#{location})")
    int saveSeller(String  name,String IdCard,String introduction,String location);

    /**
     * 查找是否存在该商家
     */
    @Select("select * from seller where name=#{name}")
    Seller selectSeller(@Param("name")String name);

    /**
     *分页管理用
     */
    @Select("SELECT seller.pictureUrl,seller.`name`   FROM seller   limit #{page},#{limit};")
    List<Seller> pageSeller(@Param("page") int page, @Param("limit") int limit);

    /**
     * 页面返回查找的信息（显示于页面上）
     */
    @Select("select seller.pictureUrl,seller.`name`  from seller where name like CONCAT('%',#{find},'%') limit #{first},#{second};")
    List<Seller> searchSeller(String find,int first,int second);

    /**
     * 商家照片
     * @param pictureUrl
     * @param id
     * @return
     */
    @Update("update seller set pictureUrl = #{pictureUrl}  where id = #{id}")
    int updateSellerImg(@Param("pictureUrl") String pictureUrl,@Param("id") Integer id);

    /**
     * 删除商家
     * @param name
     */
    @Delete("DELETE FROM seller WHERE `name`=#{name}")
    void deleteSeller(String name);
}
