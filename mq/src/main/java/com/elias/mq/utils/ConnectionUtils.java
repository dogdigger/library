package com.elias.mq.utils;

import com.elias.mq.config.RabbitMQ_Config;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 8:55 上午</p>
 * <p>description: </p>
 */
public final class ConnectionUtils {
    private ConnectionUtils() {
    }

    public static ConnectionFactory getConnectionFactory(String host, int port, String virtualHost, String userName, String password) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQ_Config.HOST);
        connectionFactory.setPort(RabbitMQ_Config.PORT);
        connectionFactory.setVirtualHost(RabbitMQ_Config.VIRTUAL_HOST);
        connectionFactory.setUsername(RabbitMQ_Config.USERNAME);
        connectionFactory.setPassword(RabbitMQ_Config.PASSWORD);
        return connectionFactory;
    }

}
