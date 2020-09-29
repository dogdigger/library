package com.elias.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:56 下午</p>
 * <p>description: 用户名和密码登录的表单</p>
 */
@Data
public class UserNameAndPasswordLoginForm {

    @JsonProperty("user_account")
    @NotBlank(message = "the user_account can not be blank")
    private String userAccount;

    @JsonProperty("user_password")
    @NotEmpty(message = "the user_password can not be empty")
    private String userPassword;
}
