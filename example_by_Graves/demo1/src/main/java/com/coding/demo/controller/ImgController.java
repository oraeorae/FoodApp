package com.coding.demo.controller;

import com.coding.demo.model.JsonResult;
import com.coding.demo.service.CompanyService;
import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping(value = "/company")
public class ImgController {
    @Autowired
    private CompanyService companyService;
    @PostMapping("/loadImg")
    public JsonResult uploadImg(@RequestPart("file") MultipartFile file, @Param(value = "id") Integer id){
        String imgPath = "D:/web/img/";//获取图片路径

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
            if (companyService.updateImgById("http://localhost:9090/img/"+newName,id) == 1){
                return new JsonResult("图片已保存");
            }
            return new JsonResult("保存失败","410","错误");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonResult("保存失败","410","错误");
}
}
