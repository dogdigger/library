package com.elias.api.auth.user;

import com.elias.common.annotation.ValidRequestHeader;
import com.elias.common.Constants;
import com.elias.common.PathDefinition;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserRegistrationForm;
import com.elias.model.view.UserInfoView;
import com.elias.response.GenericResponse;
import com.elias.service.manage.UserServiceManager;
import com.elias.validator.AuthorizationHeaderClientTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/28 10:02 上午</p>
 * <p>description: </p>
 */
@RestController
@RequestMapping(PathDefinition.URI_API_USER)
@Slf4j
public class UserController {
    private final UserServiceManager userServiceManager;

    public UserController(UserServiceManager userServiceManager) {
        this.userServiceManager = userServiceManager;
    }

    /**
     * 用户注册。该行为会导致两件事情
     * （1）创建一条User记录
     * （2）创建一条Account记录
     *
     * @param userRegistrationForm 封装用户注册所必须填的信息
     */
    @ValidRequestHeader(headerName = Constants.HEADER_AUTHORIZATION, validator = AuthorizationHeaderClientTokenValidator.class)
    @PostMapping("/actions/user-registration")
    public GenericResponse<String> userRegistration(@RequestBody @Valid UserRegistrationForm userRegistrationForm) {
        userServiceManager.userRegistration(userRegistrationForm);
        return GenericResponse.ok();
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
        if (StringUtils.isEmpty(mobile) || !mobile.matches(Constants.REGEXP_MOBILE)) {
            throw new RestException(ErrorCode.PARAM_INVALID);
        }
        // 2、生成验证码
        String verifyCode = userServiceManager.sendVerifyCode(mobile);
        // 3、发送验证码
        return GenericResponse.ok(verifyCode);
    }

    @GetMapping("/info")
    public GenericResponse<UserInfoView> findUserInfo(@RequestParam(name = "userId", required = false) UUID userId,
                                                      @RequestParam(name = "mobile", required = false) String mobile,
                                                      @RequestParam(name = "email", required = false) String email,
                                                      @RequestParam(name = "clientUserId", required = false) UUID clientUserId) {
        if (userId == null && StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email) && clientUserId == null) {
            throw new RestException(ErrorCode.PARAM_INVALID, "userId、mobile、email、clientUserId至少要有一个不为空");
        }
        return GenericResponse.ok(userServiceManager.getUserInfo(userId, mobile, email, clientUserId));
    }
}
