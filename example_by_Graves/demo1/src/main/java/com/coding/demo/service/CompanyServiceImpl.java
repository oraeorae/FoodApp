package com.coding.demo.service;

import com.coding.demo.model.Food;
import com.coding.demo.mapper.CompanyMapper;
import com.coding.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public int updateImgById(String businessLicenseUrl, Integer id) {
        return companyMapper.updateImgById(businessLicenseUrl, id);
    }

}
