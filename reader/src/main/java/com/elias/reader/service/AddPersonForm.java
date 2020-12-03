package com.elias.reader.service;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author chengrui
 * <p>create at: 2020/12/1 9:59 上午</p>
 * <p>description: </p>
 */
@Data
public class AddPersonForm {
    @NotBlank
    private String name;

    private String gender = "male";

    @Max(value = 130)
    @Min(value = 1)
    private Integer age;
}
