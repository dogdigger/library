package com.elias.mq.producer;

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
    private static final String QUEUE_NAME = "test_queue";

    public static void sendMessage(){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try(
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();){
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //channel.basicPublish();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
