package com.elias.config;

/**
 * @author chengrui
 * <p>create at: 2020/9/3 7:23 下午</p>
 * <p>description: </p>
 */
public final class Constants {
    private Constants(){}

    /**
     * 一天之内密码最多能输错的次数
     */
    public static final Integer MAX_WRONG_TIMES_ONE_DAY = 3;

    /**
     * http basic认证的header头前缀
     */
    public static final String AUTHORIZATION_BASIC = "Basic ";

    public static final String REGEX_MOBILE = "";

    /**
     * 审核状态：1:未审核, 2:审核不通过, 3:审核通过
     */
    public static enum ReviewStatus {
        UNREVIEWED, REVIEW_NOT_PASSED, REVIEW_PASSED;

        public Integer getValue() {
            return this.ordinal() + 1;
        }
    }
}
