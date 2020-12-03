package com.elias.reader.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chengrui
 * <p>create at: 2020/12/1 9:31 上午</p>
 * <p>description: </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    private String id;
    private String name;
    private String gender;
    private Integer age;
}
