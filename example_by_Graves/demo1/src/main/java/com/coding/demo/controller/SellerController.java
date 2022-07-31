package com.coding.demo.controller;

import com.coding.demo.model.Seller;
import com.coding.demo.model.User;
import com.coding.demo.service.SellerService;
import com.coding.demo.service.SellerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/Seller")
public class SellerController {
    @Autowired
    private SellerServiceImpl sellerService;
    @PostMapping("/register")
    public String register(String  name,String IdCard,String introduction,String location){
        log.info("name:{}",name);
        log.info("IdCard:{}",IdCard);
        log.info("introduction:{}",introduction);
        log.info("location:{}",location);
        if(StringUtils.isEmpty(name)){
            return "店铺名不许为空";
        }
        if(StringUtils.isEmpty(IdCard)){
            return "身份证不能为空";
        }
        if(StringUtils.isEmpty(introduction)){
            return "店铺介绍不能为空";
        }
        if(StringUtils.isEmpty(location)){
            return "位置不能为空";
        }
        Seller seller= sellerService.selectSeller(name);
        if(seller !=null){
            return "商家注册失败，用户名已经存在";
        }
        int resultCount=sellerService.insertSeller(name, IdCard, introduction, location);
        if (resultCount==0){return "申请失败";}
        return "申请成功";
    }
}
