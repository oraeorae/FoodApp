package com.example.spring_food.service;

import com.example.spring_food.pojo.LoginUser;
import com.example.spring_food.pojo.User;
import com.example.spring_food.utils.JwtUtils;
import com.mysql.cj.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authentication)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，生成JWT
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        //生成jiw
        JwtUtils jwt = JwtUtils.getInstance();
        String token = jwt
                .setClaim("username",loginUser.getUsername())
                //           .setClaim("name",name)
                //          .setClaim("avatarUrl",avatarUrl)
                .generateToken();
        return token;
    }
}
