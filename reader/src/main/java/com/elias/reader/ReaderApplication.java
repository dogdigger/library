package com.elias.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 20:04</p>
 * <p>description: </p>
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo-consumer.xml"})
public class ReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReaderApplication.class, args);
    }
}
