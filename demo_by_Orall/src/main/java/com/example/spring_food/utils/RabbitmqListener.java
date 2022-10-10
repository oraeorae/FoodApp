package com.example.spring_food.utils;

import com.alibaba.fastjson.JSON;
import com.example.spring_food.pojo.CanalMessage;
import com.example.spring_food.pojo.User;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;

@Component
public class RabbitmqListener {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "binlog"), exchange = @Exchange(value = "mysql")))
    public void businessQueue(@Payload byte[] message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            // canal发送到rabbitmq的消息默认为二进制字节流，无法看懂，所以将二进制字节流转换为String类型
            String realMessage = new String(message, StandardCharsets.UTF_8);
            System.out.println(realMessage);
            // 将String转换为对象类型
            CanalMessage<User> canalMessage = JSON.parseObject(realMessage, CanalMessage.class);
            // 只针对food数据库中的users表
            if ("food".equals(canalMessage.getDatabase()) && "users".equals(canalMessage.getTable())) {
                if ("UPDATE".equals(canalMessage.getType()) || "INSERT".equals(canalMessage.getType())) {
                    // userList不能直接等于canalMessage.getData()，否则会出现类型无法转换问题
                    List<User> userList = JSON.parseArray(JSON.parseObject(realMessage).getString("data"), User.class);
                    System.out.println("正在改变");
                    for (User user : userList) {
                        logger.info(user.toString());
                        redisTemplate.opsForValue().set("user_" + user.getId(), user, Duration.ofSeconds(60 * 60 + new Random().nextInt(60 * 10)));
                    }
                } else if ("DELETE".equals(canalMessage.getType())) {
                    List<User> userList = JSON.parseArray(JSON.parseObject(realMessage).getString("data"), User.class);
                    for (User user : userList) {
                        redisTemplate.delete("user_" + user.getId());
                    }
                }
            }
            // 手动ack,确认消息已被消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // requeue=false 表示被拒绝的消息进入死信队列
            channel.basicNack(deliveryTag, false, false);
            e.printStackTrace();
        }
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "dead.letter.queue"), exchange = @Exchange(value = "dead.letter.exchange")))
    public void deadLetterQueue(@Payload byte[] message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        logger.info("死信队列业务逻辑");
    }
}