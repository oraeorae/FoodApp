package com.coding.demo.service;

import com.coding.demo.mapper.SellerMapper;
import com.coding.demo.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService{
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public int insertSeller(String name, String IdCard, String introduction, String location) {
        return sellerMapper.saveSeller(name, IdCard, introduction, location);
    }

    @Override
    public Seller selectSeller(String name) {
        return sellerMapper.selectSeller(name);
    }
}
