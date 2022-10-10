package com.example.spring_food.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public Exchange exchange() {
        // 创建一个Direct Exchange，设置为持久化，不自动删除
        System.out.println("创建一个Direct Exchange，设置为持久化，不自动删除");
        return new DirectExchange("mysql", true, false);
    }

    @Bean
    public Exchange deadLetterExchange() {
        // 死信Exchange

        System.out.println("创建 死信Exchange");
        return new DirectExchange("dead.letter.exchange", true, false);
    }


    @Bean
    public Queue queue() {
        /**
         *  durable=true 持久化queue的元数据
         *  exclusive = false 队列不独占，允许多个消费者访问
         *  autoDelete = false 当最后一个消费者断开连接之后队列是否自动被删除
         */
        Map<String, Object> args = new HashMap<>();
        // 配置当前队列绑定的死信交换器
        //args.put("x-dead-letter-exchange", "dead.letter.exchange");
        // 配置当前队列的死信队列路由key，如果不设置默认为当前队列的路由key
        //args.put("x-dead-letter-routing-key", "dead.letter.routing.key");
        //@Argument(name = "x-message-ttl",value = "3000",type = "java.lang.Long") //指定队列的过期时间
        System.out.println("创建队列");
        return new Queue("binlog", true, false, false, args);
    }

    @Bean
    public Queue deadLetterQueue() {
        // 死信Queue
        System.out.println("创建死信Queue");
        return new Queue("dead.letter.queue", true, false, false);
    }

    @Bean
    public Binding binding() {
        // 将上面的mysql Exchange与binlog Queue以"mysql-binlog"为路由键进行绑定，无参数
        System.out.println("绑定");
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with("mysql-binlog")
                .noargs();
    }

    @Bean
    public Binding deadLetterBinding() {
        // 绑定死信Queue与死信Exchange
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dead.letter.routing.key")
                .noargs();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 开启强制委托模式-
        rabbitTemplate.setMandatory(true);
        // ack=true表示Exchange接收到了消息
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                    if (ack) {
                        logger.info("消息已发送到Exchange");
                    } else {
                        logger.error("消息未能发送到Exchange,{}", cause);
                    }
                }
        );
        // 当消息发送给Exchange后，Exchange路由到Queue失败时会执行ReturnCallBack
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
                logger.error("mq消息不可达,message:{},replyCode:{},replyText:{},exchange:{},routing:{}",
                        message, replyCode, replyText, exchange, routingKey)
        );
        return rabbitTemplate;
    }
}
