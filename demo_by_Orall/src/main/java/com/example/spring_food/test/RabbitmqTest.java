package com.example.spring_food.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//https://www.cnblogs.com/Nickc/p/11936609.html
//https://blog.csdn.net/qq_41885819/article/details/112933785
//https://blog.csdn.net/qq_37280924/article/details/124973811
public class RabbitmqTest {
    // 注入一个RabbitMQ的模板对象，操作消息队列的对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * (1) Hello World模型
     * 这是一种简单的直连模型，生产者将消息直接发送至消息队列，消费者绑定消息队列后直接获取，一对一。
     * spring-boot-starter-amqp为我们提供了一个org.springframework.amqp.rabbit.core.RabbitTemplate类来方便我们使用rabbitmq，自动注入即可。
     */

    @Test
    public void sendQueue(){
        System.out.println("开始向队列中发送一条消息！");
        // 参数1：管理中的队列名  参数2：发送的消息
        rabbitTemplate.convertAndSend("mysql","message:这是一条消息！");
        System.out.println("消息发送完毕！");
    }


}
