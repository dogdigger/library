package com.elias.exception;

import org.springframework.http.HttpStatus;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:34 下午</p>
 * <p>description: </p>
 */
public enum ErrorCode {

    // ------------------------------------------- bad request(400): 10000 -------------------------------------------
    /**
     * 参数错误
     */
    PARAM_INVALID(HttpStatus.BAD_REQUEST, 10000, "param invalid"),

    /**
     * 重复的客户端名称
     */
    DUPLICATE_CLIENT_NAME(HttpStatus.BAD_REQUEST, 10001, "duplicate client name"),

    /**
     * Authorization请求头错误
     */
    WRONG_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, 10002, "wrong request header `Authorization`"),

    /**
     * 客户端secret或者用户密码错误
     */
    WRONG_SECRET_OR_PASSWORD(HttpStatus.BAD_REQUEST, 10003, "wrong secret or password"),

    /**
     * 不支持的令牌类型
     */
    UNSUPPORTED_TOKEN_TYPE(HttpStatus.BAD_REQUEST, 10004, "unsupported token type"),

    /**
     * 验证码已过期
     */
    EXPIRED_VERIFY_CODE(HttpStatus.BAD_REQUEST, 10005, "the verify code has expired"),

    /**
     * 不支持的登录方式
     */
    UNSUPPORTED_LOGIN_TYPE(HttpStatus.BAD_REQUEST, 10006, "unsupported login type"),

    /**
     * 该手机号码还未进行注册
     */
    UNREGISTERED_MOBILE(HttpStatus.BAD_REQUEST, 10007, "the mobile has not been registered"),


    // ------------------------------------------- unauthorized(401): 20000 -------------------------------------------
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 20000, "the request is unauthorized"),

    /**
     * 令牌已过期
     */
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 20001, "the accessToken has expired"),


    // ------------------------------------------- forbidden(403): 30000 -------------------------------------------

    // ------------------------------------------- not found(404): 40000 -------------------------------------------
    /**
     * 找不带对应的资源
     */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 40000, "resource not found"),

    /**
     * 找不到对应的客户端
     */
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, 40001, "client not found"),

    /**
     * 不存在的访问令牌
     */
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 40002, "Nonexistent accessToken"),

    /**
     * 不存在的手机验证码
     */
    VERIFY_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, 40003, "Nonexistent verifyCode"),


    // ----------------------------------------- internal error(500): 50000 ----------------------------------------
    /**
     * 配置项错误
     */
    WRONG_CONFIGURATION_ITEM(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "wrong configuration item");


    private final HttpStatus httpStatus;
    private final int businessCode;
    private String errorMessage;

    ErrorCode(HttpStatus httpStatus, int businessCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.businessCode = businessCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public int getBusinessCode() {
        return this.businessCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
