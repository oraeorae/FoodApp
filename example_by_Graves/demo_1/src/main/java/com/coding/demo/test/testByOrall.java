package com.coding.demo.test;

import com.coding.demo.annotations.Limit;
import com.coding.demo.service.RecommendService;
import com.coding.demo.utils.SensitiveUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class testByOrall {
    @Autowired
    private SensitiveUtils sensitiveUtils;

    /**
     * 测试敏感词过滤
     */
    @Test
    public void testSensitive(){
        String word = "不如去美团看，这个软件不行";
        String text = sensitiveUtils.filter(word);
        System.out.println(text);
        return;
    }

    @Autowired
    private RecommendService recommendService;

    /**
     * 测试更新或者插入用户数据
     * 收藏和浏览都应该执行这个
     */
    @Test
    public void testUpdate(){
        int userid = 6;
        int businessid = 10;
        //判断当前有没有该用户的商店数据
        if( recommendService.isUserActive(userid,businessid) == 0 ){        //没有数据则插入
            recommendService.insertUserActive(userid,businessid);
        }else{          //已经存在则更新
            recommendService.updateUserActive(userid,businessid);
        }
    }


    /**
     *
     */
    @Test
    @Limit(key = "testLimit", permitsPerSecond = 1, timeout = 500, msg = "请求过于频繁,请稍后再试！")
    @GetMapping(value = "testLimit")
    public void testLimit(){

    }


}
