package com.elias.book.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengrui
 * <p>create at: 2020-07-27 17:40</p>
 * <p>description: </p>
 */
@Data
public class Book implements Serializable {
    private String name;
    private String author;
}
