package com.coding.demo.utils;


import io.jsonwebtoken.Claims;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理token相关工具类
 */
public class UserRequestUtils {
    public static String getCurrentToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("token");
        return token;
    }

    public static String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("token");

        //获取请求时的token
        JwtUtils jwt = JwtUtils.getInstance();
        Claims claims = jwt.check(token);
        String name = "";
        if (claims != null) {
            //获取name
            name = (String) claims.get("name");
        }
        return name;
    }

    public static String getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("token");

        //获取请求时的token
        JwtUtils jwt = JwtUtils.getInstance();
        Claims claims = jwt.check(token);
        String id = "";
        if (claims != null) {
            //获取id
            id = (String) claims.get("id");
        }
        return id;
    }
}
