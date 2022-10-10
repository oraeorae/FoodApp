package com.example.spring_food.utils;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue("hello"))
public class HelloQueuesConsumer {
    @RabbitHandler
    public void consume(String msg){
        System.out.println("消费消息：" + msg + " " + System.currentTimeMillis());
    }
}
