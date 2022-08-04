package com.example.spring_food.aop.Limit;

public enum LimitType {
    /**
     * 默认策略全局限流
     */
    CUSTOMER,
    /**
     * 根据请求者IP进行限流
     */
    IP
}