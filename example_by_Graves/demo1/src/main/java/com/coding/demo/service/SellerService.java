package com.coding.demo.service;

import com.coding.demo.model.Seller;

public interface SellerService {
    /**
     * 申请成为商家
     * @param name
     * @param IdCard
     * @param introduction
     * @param location
     * @return 申请成功几个
     */
    int insertSeller(String name,String IdCard,String introduction,String location);

    Seller selectSeller(String name);
}
