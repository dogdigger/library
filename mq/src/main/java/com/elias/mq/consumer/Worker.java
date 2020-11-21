package com.elias.mq.consumer;

import com.elias.mq.config.RabbitMQ_Config;
import com.elias.mq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/10/9 9:54 上午</p>
 * <p>description: </p>
 */
public class Worker {
    public void doWork(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    public void process() throws IOException, TimeoutException {
        ConnectionFactory factory = ConnectionUtils.getConnectionFactory();
        try(
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();)
        {
            channel.queueDeclare(RabbitMQ_Config.QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (tag, delivery) -> {
                String body = new String(delivery.getBody());
                System.out.println("received a message: " + body);
            };
            channel.basicConsume(RabbitMQ_Config.QUEUE_NAME, false, deliverCallback, System.out::println);
        }
    }
}
