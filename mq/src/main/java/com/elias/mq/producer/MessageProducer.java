package com.elias.mq.producer;

import com.elias.mq.config.RabbitMQ_Config;
import com.elias.mq.message.SleepTask;
import com.elias.mq.utils.ConnectionUtils;
import com.elias.mq.utils.JSONUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/8/5 5:08 下午</p>
 * <p>description: </p>
 */
public class MessageProducer {

    public static void sendMessage(Object msgObj) {
        // 将对象转成json格式
        String msg = JSONUtils.objToJson(msgObj);
        if (msg == null) {
            throw new RuntimeException(String.format("can not serialize %s to json", msgObj));
        }
        sendMessage(msg);
    }

    public static void sendMessage(String message) {
        System.out.println("prepare to send message......");
        // log.info("prepare to send message......");
        ConnectionFactory factory = ConnectionUtils.getConnectionFactory();
        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel();) {
            // 声明队列是幂等的，即只有当队列不存在的时候才会创建队列
            // durable  是否需要持久化队列。如果持久化队列，那么服务器重启后队列还在
            // exclusive    是否是独占队列。独占队列是指只有当前connection才能使用
            // autoDelete   是否自动删除。当队列不被使用的时候，服务器就会将队列删除
            channel.queueDeclare(RabbitMQ_Config.QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", RabbitMQ_Config.QUEUE_NAME, null, message.getBytes());
            System.out.println("message " + message + " send successfully");
            // log.info("message {} send successfully", message);
        } catch (TimeoutException | IOException e) {
            // log.error("message {} send failed", message);
            System.err.println("message " + message + " send failed");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            sendMessage(new SleepTask(i,1500));
        }
    }
}
