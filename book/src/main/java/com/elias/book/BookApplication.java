package com.elias.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 19:50</p>
 * <p>description: 启动类</p>
 */
@SpringBootApplication
@ImportResource({"classpath:dubbo-provider.xml"})
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
