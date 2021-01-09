package com.elias.common;

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

    /**
     * 配置项名称：客户端令牌的有效时间，单位为毫秒
     */
    public static final String ENV_CLIENT_TOKEN_EXPIRE_KEY = "expire.token.client";

    /**
     * 配置项名称：用户令牌的有效时间，单位为毫秒
     */
    public static final String ENV_USER_TOKEN_EXPIRE_KEY = "expire.token.user";

    /**
     * 分布式锁的过期时间，单位毫秒
     */
    public static final long DISTRIBUTED_LOCK_EXPIRE = 2000;

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

    /**
     * 默认的用户显示名称
     */
    public static final String DEFAULT_DISPLAY_NAME = "匿名用户";

    /**
     * 默认的用户头像地址
     */
    public static final String DEFAULT_AVATAR = "";

    /**
     * 数据在redis中的过期时间枚举，单位为秒
     */
    public static enum ExpireTimeEnum {
        /**
         * 分钟
         */
        MINUTE(60),

        /**
         * 1小时
         */
        HOUR(60*60),

        /**
         * 1天
         */
        DAY(24*60*60),

        /**
         * 1周
         */
        WEEK(7*24*60*60),

        /**
         * 1个月
         */
        MONTH(30*24*60*60);

        private final long expire;

        ExpireTimeEnum(long expire) {
            this.expire = expire;
        }

        /**
         * 以秒为单位
         * @return
         */
        public long getTime() {
            return expire;
        }
    }

    public static void main(String[] args) {
        System.out.println("15926643146".matches(REGEXP_MOBILE));
    }
}
