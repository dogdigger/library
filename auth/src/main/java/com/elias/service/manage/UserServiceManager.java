package com.elias.service.manage;

import com.elias.entity.ClientUser;
import com.elias.entity.User;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import com.elias.model.form.user.UserRegistrationForm;
import com.elias.model.view.UserInfoView;
import com.elias.service.AccountService;
import com.elias.service.ClientUserService;
import com.elias.service.UserService;
import com.elias.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2021/1/9 9:32 上午</p>
 * <p>description: </p>
 */
@Service
@Slf4j
public class UserServiceManager {
    private final UserService userService;
    private final AccountService accountService;
    private final VerifyCodeService verifyCodeService;
    private final ClientUserService clientUserService;

    public UserServiceManager(UserService userService,
                              AccountService accountService,
                              VerifyCodeService verifyCodeService,
                              ClientUserService clientUserService) {
        this.userService = userService;
        this.accountService = accountService;
        this.verifyCodeService = verifyCodeService;
        this.clientUserService = clientUserService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void userRegistration(UserRegistrationForm userRegistrationForm) {
        // 1、创建用户
        User user = userService.createUser(userRegistrationForm);
        // 2、为用户创建账号
        accountService.createAccount(user.getId());
    }

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     */
    public String sendVerifyCode(String mobile) {
        // 1、验证手机号码是否已注册
        User user = userService.findUserByMobile(mobile);
        if (user == null) {
            throw new RestException(ErrorCode.UNREGISTERED_MOBILE);
        }
        return verifyCodeService.createVerifyCode(mobile).getCode();
    }

    public UserInfoView getUserInfo(UUID userId, String mobile, String email, UUID clientUserId) {
        User user;
        ClientUser clientUser = null;
        if (userId != null) {
            user = userService.findUserById(userId);
        }else if (!StringUtils.isEmpty(mobile)) {
            user = userService.findUserByMobile(mobile);
        }else if (!StringUtils.isEmpty(email)) {
            user = userService.findByEmail(email);
        }else {
            clientUser = clientUserService.findByClientUserId(clientUserId);
            user = userService.findUserById(clientUser.getUserId());
        }
        if (user == null) {
            throw new RestException(ErrorCode.USER_NOT_FOUND);
        }
        return UserInfoView.create(user, clientUser);
    }


}
