package com.example.spring_food.service;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*** 消费者*/
@Component
@RabbitListener(queuesToDeclare = @Queue("mysql"))
public class Receiver {

    @RabbitHandler
    public void process(String s) {
        System.out.println("Receiver: 消费成功" + s);
    }
}