package com.elias.model.form.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author chengrui
 * <p>create at: 2020/11/18 5:14 下午</p>
 * <p>description: </p>
 */
@Data
public class ClientCreateForm {

    /**
     * 客户端名称，长度不超过50
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * 客户端简介，长度不超过100
     */
    @NotBlank
    @Size(max = 100)
    private String description;
}
