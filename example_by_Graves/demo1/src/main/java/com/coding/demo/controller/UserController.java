package com.coding.demo.controller;

import com.coding.demo.mapper.UserMapper;
import com.coding.demo.model.User;
import com.coding.demo.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Random;

@Slf4j
@RequestMapping("/user")
@RestController
@ResponseBody
public class UserController {
    @Autowired
    private UserServiceImpl userService;


    @Resource
    private JavaMailSenderImpl mailSender;

    @PostMapping("/login")//登录
    public String login(String name,String password){
        User user=userService.selectUser(name);
        if(StringUtils.isEmpty(name)){
            return "用户名不许为空";
        }
        if(StringUtils.isEmpty(password)){
            return "密码不能为空";
        }
        if(user==null){
            return "登录失败,找不到该用户";
        }
        if(Objects.equals(user.getPassword(), password)){
            return "登录成功";
        }
        return "登录失败,密码错误";
    }


    @PostMapping("/register")//注册，姓名和密码
    public String register(String name,String password){
        log.info("name:{}",name);
        log.info("password:{}",password);
        if(StringUtils.isEmpty(name)){
            return "用户名不许为空";
        }
        if(StringUtils.isEmpty(password)){
            return "密码不能为空";
        }
        User user=userService.selectUser(name);
        if(user !=null){
            return "注册失败，用户已经存在";
        }
        int resultCount=userService.insertUser(name,password);
        if(resultCount==0){return "注册失败";}
        return "注册成功";
    }


    @PostMapping("/updateUser")//更新信息，添加邮箱和电话，需要提供姓名
    public String updateUser(String name,String  email,String phone){
        log.info("name:{}",name);
        log.info("phone:{}",phone);
        log.info("email:{}",email);
        if(StringUtils.isEmpty(phone)){
            return "电话不许为空";
        }
        if(StringUtils.isEmpty(email)){
            return "邮箱不能为空";
        }
       if(userService.updateUser(name, email, phone)==1)
        return "更新成功";
        return "更新失败";
    }

    @GetMapping("/test")
    public String test1(){
        return "测试成功";
    }

    @GetMapping("/sendmail")//发送邮箱
    public String send(String email) throws MessagingException {
        int count=1;//默认发送一次
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        while(count--!=0){
            String codeNum="";
            int []code =  new int[3];
            Random random=new Random();
            //自动生成验证码
            for(int i=0;i<6;i++){
                int num=random.nextInt(10)+48;
                int uppercase = random.nextInt(26)+65;
                int lowercase = random.nextInt(26)+97;
                code[0]=num;
                code[1]=uppercase;
                code[2]=lowercase;
                codeNum += (char)code[random.nextInt(3)];
            }
            System.out.println(codeNum);
            //标题
            helper.setSubject("您的验证码为："+codeNum);
            //内容
            helper.setText("您好！感谢支持，您的验证码为："+codeNum+"   不要透露给别人");
            //邮箱接受者
            helper.setTo(email);
            helper.setFrom("870519251@qq.com");
            mailSender.send(mimeMessage);
    }
        return "邮件发送成功";
    }

    @PostMapping("/updatepassword")
    public String updatePassword(String name,String password){
        log.info("name:{}",name);
        log.info("password:{}",password);
        if(StringUtils.isEmpty(name)){
            return "用户名不许为空";
        }
        if(StringUtils.isEmpty(password)){
            return "密码不能为空";
        }
        User user=userService.selectUser(name);
        if(user ==null){
            return "修改失败，该用户不存在";
        }
        int  count =userService.updatePassword(name, password);
        if(count==0)return "修改失败";
        return "修改成功";
    }
}
