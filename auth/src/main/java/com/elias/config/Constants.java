package com.elias.config;

/**
 * @author chengrui
 * <p>create at: 2020/9/3 7:23 下午</p>
 * <p>description: </p>
 */
public final class Constants {
    private Constants(){}

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String HEADER_AUTHORIZATION_BEARER_PREFIX = "Bearer ";

    public static final String HEADER_AUTHORIZATION_BASIC_PREFIX = "Basic ";

    public static final String CLIENT_TOKEN_EXPIRE_KEY = "expire.token.client";

    public static final String USER_TOKEN_EXPIRE_KEY = "expire.token.user";

    /**
     * 给密码随机加盐的长度，固定为16
     */
    public static final int PASSWORD_SALT_LENGTH = 16;

    /**
     * 生成账号时，初始密码的长度(初始密码是随机的，后面用户必须要通过手机验证码进行修改)
     */
    public static final int INITIAL_PASSWORD_LENGTH = 10;

    /**
     * 手机号码的正则表达式
     */
    public static final String REGEXP_MOBILE = "1[3-9]\\d{9}";

    public static void main(String[] args) {
        System.out.println("15926643146".matches(REGEXP_MOBILE));
    }
}
