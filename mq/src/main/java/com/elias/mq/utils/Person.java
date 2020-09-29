package com.elias.mq.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chengrui
 * <p>create at: 2020/9/29 10:07 上午</p>
 * <p>description: </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private char gender;
    private Integer age;
}
