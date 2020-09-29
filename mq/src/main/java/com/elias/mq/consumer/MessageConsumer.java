package com.elias.mq.consumer;

import com.elias.mq.config.RabbitMQ_Config;
import com.elias.mq.utils.ConnectionUtils;
import com.elias.mq.utils.JSONUtils;
import com.elias.mq.utils.Person;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 2:42 下午</p>
 * <p>description: </p>
 */
public class MessageConsumer {

    public static void consume() throws IOException, TimeoutException {
        ConnectionFactory factory = ConnectionUtils.getConnectionFactory();
        try(Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();) {
            channel.queueDeclare(RabbitMQ_Config.QUEUE_NAME, false, false, false, null);
            DeliverCallback deliverCallback = (tag, delivery) -> {
                String body = new String(delivery.getBody());
                System.out.println("received a message: " + body);
                System.out.printf("serialize the body into Person instance: %s", JSONUtils.jsonToObj(body, Person.class));
            };
            channel.basicConsume(RabbitMQ_Config.QUEUE_NAME, deliverCallback, System.out::println);
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        consume();
    }

}
