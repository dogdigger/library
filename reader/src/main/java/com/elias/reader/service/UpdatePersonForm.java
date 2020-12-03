package com.elias.reader.service;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author chengrui
 * <p>create at: 2020/12/1 10:10 上午</p>
 * <p>description: </p>
 */
@Data
public class UpdatePersonForm {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String gender;

    @NotNull
    private Integer age;
}
