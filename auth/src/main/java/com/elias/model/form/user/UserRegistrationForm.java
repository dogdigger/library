package com.elias.model.form.user;

import com.elias.config.Constants;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 4:57 下午</p>
 * <p>description: 用户注册</p>
 */
@Data
public class UserRegistrationForm {
    /**
     * 用户姓名
     */
    @Size(max = 50)
    @NotBlank
    private String name;

    /**
     * 用户性别，默认为1，即男性
     */
    private Integer gender = 1;

    /**
     * 头像地址
     */
    @Size(max = 100)
    private String avatar;

    /**
     * 手机号码
     */
    @NotBlank
    @Pattern(regexp = Constants.REGEXP_MOBILE)
    private String mobile;

    /**
     * 邮箱
     */
    @Email
    private String email;
}
