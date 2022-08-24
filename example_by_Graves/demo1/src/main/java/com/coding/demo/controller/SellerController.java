package com.coding.demo.controller;

import com.coding.demo.model.JsonResult;
import com.coding.demo.model.Seller;
import com.coding.demo.model.User;
import com.coding.demo.service.SellerService;
import com.coding.demo.service.SellerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerServiceImpl sellerService;
    @PostMapping("/register")
    public JsonResult register(String  name,String IdCard,String introduction,String location){
        log.info("name:{}",name);
        log.info("IdCard:{}",IdCard);
        log.info("introduction:{}",introduction);
        log.info("location:{}",location);
        try{
            if(StringUtils.isEmpty(name)) return new JsonResult("用户名不能为空","410","fail");
            if(StringUtils.isEmpty(IdCard)) return new JsonResult("身份证不能为空","410","fail");
            if(StringUtils.isEmpty(introduction)) return new JsonResult("介绍不能为空","410","fail");
            if(StringUtils.isEmpty(location)) return new JsonResult("位置不能为空","410","fail");

            Seller seller= sellerService.selectSeller(name);
            if(seller !=null)return new JsonResult("该用户已经存在","410","fail");

            if(sellerService.insertSeller(name, IdCard, introduction, location)!=0) {
                return new JsonResult("申请成功");
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult("申请失败","410","fail");
    }

    @PostMapping("/show")
    public JsonResult listSeller(int page){
            try{

                List<Seller> tmp=sellerService.pageSeller(page,8);
                return new JsonResult("返回成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonResult("服务器内部错误","410","错误");
            }
    }
    @PostMapping("/search")
    public JsonResult search( String find,int page) {
        //limit 为8
        try {
            List<Seller> tmp = sellerService.searchSeller(find,page,8);
            return new JsonResult("查找成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器内部错误","410","错误");
        }
    }

    @PostMapping("/loadImg")
    public JsonResult uploadImg(@RequestPart("file") MultipartFile file, @Param(value = "id") Integer id){
        String imgPath = "D:/web/img/seller/";//获取图片路径

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
            if (sellerService.updateSellerImg("http://localhost:9090/img/seller/"+newName,id) == 1){
                return new JsonResult("图片已保存");
            }
            return new JsonResult("保存失败","410","错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonResult("保存失败","410","错误");
    }

    @PostMapping("/delete")
    public JsonResult delete(String name){
        try{
            if(sellerService.selectSeller(name)==null)
            {
                return new JsonResult("删除失败，不存在该用户","410","失败");
            }
            else {
                sellerService.deleteSeller(name);
                return new JsonResult("删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("服务器异常","410","失败");
        }

    }
}
