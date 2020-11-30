package com.elias.api.auth.user;

import com.elias.common.annotation.ValidRequestHeader;
import com.elias.config.Constants;
import com.elias.config.PathDefinition;
import com.elias.entity.User;
import com.elias.entity.VerifyCode;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserRegistrationForm;
import com.elias.response.GenericResponse;
import com.elias.service.AccountService;
import com.elias.service.UserService;
import com.elias.service.VerifyCodeService;
import com.elias.validator.AuthorizationHeaderClientTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author chengrui
 * <p>create at: 2020/11/28 10:02 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.URI_API_USER)
@Slf4j
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final VerifyCodeService verifyCodeService;

    public UserController(UserService userService, AccountService accountService, VerifyCodeService verifyCodeService) {
        this.userService = userService;
        this.accountService = accountService;
        this.verifyCodeService = verifyCodeService;
    }

    /**
     * 用户注册
     *
     * @param userRegistrationForm 封装用户注册所必须填的信息
     */
    @ValidRequestHeader(headerName = Constants.HEADER_AUTHORIZATION, validator = AuthorizationHeaderClientTokenValidator.class)
    @PostMapping("/actions/user-registration")
    public GenericResponse<String> userRegistration(@RequestBody @Valid UserRegistrationForm userRegistrationForm) {
        // 1、创建user
        User user = userService.createUser(userRegistrationForm);
        // 2、创建account
        accountService.createAccount(user);
        return new GenericResponse<>(null);
    }

    /**
     * 发送验证码
     *
     * @param mobile 发送目标
     * @return {@link GenericResponse<String>}
     */
    @ValidRequestHeader(headerName = Constants.HEADER_AUTHORIZATION, validator = AuthorizationHeaderClientTokenValidator.class)
    @GetMapping("/verify-codes")
    public GenericResponse<String> sendVerifyCode(@RequestParam(name = "mobile") String mobile) {
        // 1、参数校验
        if (StringUtils.isEmpty(mobile) || mobile.matches(Constants.REGEXP_MOBILE)) {
            throw new RestException(ErrorCode.PARAM_INVALID);
        }
        // 2、检查手机号收否已注册
        User user = userService.findUserByMobile(mobile);
        if (user == null) {
            throw new RestException(ErrorCode.UNREGISTERED_MOBILE);
        }
        // 3、生成验证码
        VerifyCode verifyCode = verifyCodeService.createVerifyCode(mobile);
        // 4、发送验证码
        log.info("为手机号码 [{}] 生成的验证码是 [{}]", mobile, verifyCode.getCode());
        return new GenericResponse<>(verifyCode.getCode());
    }
}
