package com.elias.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chengrui
 * <p>create at: 2020/10/9 10:12 上午</p>
 * <p>description: </p>
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SleepTask implements Task {
    private Integer seqNo;
    private Integer sleepTime;

    @Override
    public void execute() {
        // log.info("begin to execute task......");
        System.out.println("begin to execute task......");
        try {
            Thread.sleep(sleepTime);
            // log.info("task finished......");
            System.out.println("task finished......");
        } catch (InterruptedException e) {
            // log.error("task failed......");
            System.err.println("task failed......");
            e.printStackTrace();
        }
    }
}
