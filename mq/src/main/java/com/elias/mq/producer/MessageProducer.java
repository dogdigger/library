package com.elias.mq.producer;

import com.elias.mq.config.RabbitMQ_Config;
import com.elias.mq.utils.ConnectionUtils;
import com.elias.mq.utils.JSONUtils;
import com.elias.mq.utils.Person;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/8/5 5:08 下午</p>
 * <p>description: </p>
 */
@Slf4j
public class MessageProducer {

    public static void sendMessage(Object msgObj){
        ConnectionFactory factory = ConnectionUtils.getConnectionFactory();
        // 将对象转成json格式
        String msg = JSONUtils.objToJson(msgObj);
        if (msg == null) {
            throw new RuntimeException(String.format("can not serialize %s to json", msgObj));
        }
        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel();){
            // 声明队列是幂等的，即只有当队列不存在的时候才会创建队列
            // durable  是否需要持久化队列。如果持久化队列，那么服务器重启后队列还在
            // exclusive    是否是独占队列。独占队列是指只有当前connection才能使用
            // autoDelete   是否自动删除。当队列不被使用的时候，服务器就会将队列删除
            channel.queueDeclare(RabbitMQ_Config.QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", RabbitMQ_Config.QUEUE_NAME, null, msg.getBytes());
            log.info("send msg: {}", msg);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendMessage(new Person("刘备", '男', 44));
    }
}
