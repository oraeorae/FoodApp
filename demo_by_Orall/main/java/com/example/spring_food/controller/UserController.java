package com.example.spring_food.controller;

import com.example.spring_food.pojo.User;
import com.example.spring_food.service.UserService;
import com.example.spring_food.utils.JwtUtils;
import com.example.spring_food.utils.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController         //注解可以使结果以Json字符串的形式返回给客户端
@RequestMapping(value = "/api/user")         //使链接还有一个 /api/
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("register")
    @ApiOperation(value="注册")
    public Map<String, Object> register(@Valid User user) {
        try{
            boolean res = userService.register(user);
            if( res == true ){
                Map<String, Object> data = StatusCode.success("注册成功");
                return data;
            }else{
                Map<String, Object> data = StatusCode.success("注册失败，用户已存在");
                return data;
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusCode.error(3001, "注册失败，服务器内部错误：" + e.toString());
        }

    }

    @PostMapping("login")
    @ApiOperation(value="登录")
    public Map<String, Object> login(@Valid User user) {
        try{
            boolean res = userService.login(user);
            if( res == true ){

                //生成token
                JwtUtils jwt = JwtUtils.getInstance();
                String token = jwt
                        .setClaim("username",user.getUsername())
                        .generateToken();
                Map<String, String> tmp  = new HashMap<>();
                tmp.put("situation","登录成功");
                tmp.put("token",token);
                Map<String, Object> data = StatusCode.success(tmp);
                return data;
            }else{
                Map<String, String> tmp  = new HashMap<>();
                tmp.put("situation","登录失败，账户或密码错误");
                tmp.put("token",null);
                return StatusCode.success(tmp);
            }
        }catch (Exception e){
            e.printStackTrace();
            return StatusCode.error(3001, "登录失败，服务器内部错误：" + e.toString());
        }

    }

}
