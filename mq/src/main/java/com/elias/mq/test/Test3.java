package com.elias.mq.test;

import com.elias.mq.consumer.MessageConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/10/9 10:53 上午</p>
 * <p>description: </p>
 */
public class Test3 {
    public static void main(String[] args) throws IOException, TimeoutException {
        new MessageConsumer("Jessica").consume();
    }
}
