package com.elias.mq.test;

import com.elias.mq.consumer.MessageConsumer;
import com.elias.mq.message.SleepTask;
import com.elias.mq.utils.JSONUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chengrui
 * <p>create at: 2020/10/9 10:31 上午</p>
 * <p>description: </p>
 */
public class Test {
    public static void main(String[] args) throws IOException, TimeoutException {
        new MessageConsumer("tom").consume();
//        SleepTask sleepTask = JSONUtils.jsonToObj("{\"sleepTime\":1000}", SleepTask.class);
//        sleepTask.execute();
    }
}
