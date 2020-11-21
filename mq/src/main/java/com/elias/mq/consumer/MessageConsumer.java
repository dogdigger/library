package com.elias.mq.consumer;

import com.elias.mq.config.RabbitMQ_Config;
import com.elias.mq.message.SleepTask;
import com.elias.mq.utils.ConnectionUtils;
import com.elias.mq.utils.JSONUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 2:42 下午</p>
 * <p>description: </p>
 */
public class MessageConsumer {
    private final String name;

    public MessageConsumer(String name) {
        this.name = name;
    }

    public void consume() throws IOException, TimeoutException {
        ConnectionFactory factory = ConnectionUtils.getConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitMQ_Config.QUEUE_NAME, false, false, false, null);
        DeliverCallback deliverCallback = (tag, delivery) -> {
            String message = new String(delivery.getBody());
            // log.info("consumer {} received message {}", name, message);
            System.out.println("consumer " + this.name + " received a message " + message);
            SleepTask task;
            try {
                task = JSONUtils.jsonToObj(message, SleepTask.class);
            } catch (IOException e) {
                // log.error("can not deserialize {} to SleepTask", message);
                e.printStackTrace();
                System.err.println("can not deserialize " + message + " to SleepTask");
                return;
            }
            task.execute();
            // 手动确认
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        // queue 队列名称
        // autoAck 是否自动确认
        // DeliverCallback 收到消息时的回调函数
        // CancelCallback 当consumer断开的时候的回调函数
        channel.basicConsume(RabbitMQ_Config.QUEUE_NAME, deliverCallback, System.out::println);
    }

}
