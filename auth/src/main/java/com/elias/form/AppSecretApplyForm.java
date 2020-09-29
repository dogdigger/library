package com.elias.form;

import com.elias.config.Constants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 7:50 下午</p>
 * <p>description: 申请appSecret的表单</p>
 */
@Data
public class AppSecretApplyForm {

    /**
     * 申请人的手机号码
     */
    @NotBlank
    @Pattern(regexp = Constants.REGEX_MOBILE)
    private String mobile;

    /**
     * 客户端名称
     */
    @NotBlank
    @Size(max = 50, message = "the length of clientName can not exceed {max}")
    private String clientName;

    @NotBlank
    @Size(max = 100, message = "the length of description can not exceed {max}")
    private String description;
}
