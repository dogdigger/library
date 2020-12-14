package com.elias.model.form.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author chengrui
 * <p>create at: 2020/11/24 5:49 下午</p>
 * <p>description: </p>
 */
@Data
public class UserLoginForm {
    /**
     * 关键字，可以是手机号码或邮箱
     */
    @NotBlank
    private String key;

    /**
     * 代码，可以是密码也可以是验证码，通过type来区分
     */
    @NotBlank
    private String code;

    /**
     * 登录的方式--目前只支持账号密码、手机号验证码两种登录方式
     * 1表示账号密码登录，2表示验证码登录
     */
    @NotNull
    private Integer type;

    public enum LoginTypeEnum {
        /**
         * 账号密码登录
         */
        PASSWORD(1),

        /**
         * 验证码登录
         */
        VERIFY_CODE(2);

        private final Integer type;

        LoginTypeEnum(Integer type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        /**
         * 判断是否是支持的登录方式
         *
         * @param type 登录方式
         * @return boolean
         */
        public static boolean isSupported(int type) {
            for (LoginTypeEnum loginType : LoginTypeEnum.values()) {
                if (loginType.getType() == type) {
                    return true;
                }
            }
            return false;
        }

        public static LoginTypeEnum loginTypeEnum(int loginType) {
            for (LoginTypeEnum typeEnum : LoginTypeEnum.values()) {
                if (loginType == typeEnum.type) {
                    return typeEnum;
                }
            }
            return null;
        }
    }
}
