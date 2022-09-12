package com.coding.demo.mapper;
import com.coding.demo.model.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CompanyMapper {
    @Update("update company set business_license_url = #{businessLicenseUrl}  where id = #{id}")
    int updateImgById(@Param("businessLicenseUrl") String businessLicenseUrl,@Param("id") Integer id);
}
