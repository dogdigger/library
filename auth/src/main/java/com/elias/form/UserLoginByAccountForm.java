package com.elias.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author chengrui
 * <p>create at: 2020/9/23 11:51 上午</p>
 * <p>description: 用户通过账号登录表单</p>
 */
@Data
public class UserLoginByAccountForm {

    /**
     * 用户名
     */
    @NotBlank
    @JsonProperty("user_name")
    private String userName;

    /**
     * 密码
     */
    @NotBlank
    @JsonProperty("user_pwd")
    private String userPwd;
}
