package com.elias.model.view;

import lombok.Data;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/18 8:02 下午</p>
 * <p>description: </p>
 */
@Data
public class ClientView {
    private UUID id;
    private String name;
    private String description;
    private String secret;
}
