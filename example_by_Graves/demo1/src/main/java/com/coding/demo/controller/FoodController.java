package com.coding.demo.controller;

import com.coding.demo.model.Food;
import com.coding.demo.model.JsonResult;
import com.coding.demo.model.Seller;
import com.coding.demo.service.FoodServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("/food")
@RestController
@ResponseBody
@Slf4j
public class FoodController {
    @Autowired
    private FoodServiceImpl foodService;

    @RequestMapping("/register")
    public JsonResult registerFood(String  name, String sellerID, String foodIntroduction, String species, String price){
        log.info("name:{}",name);
        log.info("sellerID:{}",sellerID);
        log.info("foodIntroduction:{}",foodIntroduction);
        log.info("species:{}",species);
        log.info("price:{}",price);
        try{
            if(StringUtils.isEmpty(name)) return new JsonResult("食物名不能为空","410","fail");
            if(StringUtils.isEmpty(sellerID)) return new JsonResult("商家店铺号不能为空","410","fail");
            if(StringUtils.isEmpty(foodIntroduction)) return new JsonResult("食物介绍不能为空","410","fail");
            if(StringUtils.isEmpty(species)) return new JsonResult("种类不能为空","410","fail");
            if(StringUtils.isEmpty(price)) return new JsonResult("价格不能为空","410","fail");
            Food food=foodService.selectFood(name);
            if(food!=null){
                return new JsonResult("添加失败，该食物名称已经存在","410","fail");
            }
            if(foodService.InsertFood(name, sellerID, foodIntroduction, species, price)!=0){
                return new JsonResult("添加成功");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new JsonResult("添加失败","410","fail");
    }

    @PostMapping("/loadImg")
    public JsonResult uploadImg(@RequestPart("file") MultipartFile file, @Param(value = "id") Integer id){
        String imgPath = "D:/web/img/food/";//获取图片路径

        String ImgName = file.getOriginalFilename();
        String lastName = ImgName.substring(ImgName.lastIndexOf(".")); //获取图片后缀名
        String newName = id+lastName;//修改图片名字 用户 id+之前图片后缀名(.png)
        File filePath = new File(imgPath,newName);//整合图片路径

        // 判断路径是否存在，如果不存在就创建一个
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        // 将上传的文件保存到一个目标文件当中
        try {
            file.transferTo(filePath);
            if (foodService.updateFoodImg("http://localhost:9090/img/food/"+newName,id)== 1){
                return new JsonResult("图片已保存");
            }
            return new JsonResult("保存失败","410","错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonResult("保存失败","410","错误");
    }

    @PostMapping("/show")
    public JsonResult listSeller(int page){
        //2022.8.24 新增或修改部分   by:Orall
        try{
            List<Food> tmp=foodService.pageFood(page,8);
            return new JsonResult(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器内部错误","410","错误");
        }
    }

    @PostMapping("/search")
    public JsonResult search( String find,int page) {
        //limit 为8
        try {
            List<Food> tmp = foodService.searchFood(find,page,8);
            return new JsonResult("查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器内部错误","410","错误");
        }
    }

    @PostMapping("/delete")
    public JsonResult delete(String name){
        try{
            if(foodService.selectFood(name)==null)
            {
                return new JsonResult("删除失败，不存在该食物","410","失败");
            }
            else {
                foodService.deleteFood(name);
                return new JsonResult("删除成功");
            }
            } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","410","失败");
        }

    }
}
