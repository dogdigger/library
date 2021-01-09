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
     * 手机号码还未进行注册
     */
    UNREGISTERED_MOBILE(HttpStatus.BAD_REQUEST, 10007, "the mobile has not been registered"),

    /**
     * 手机号码已被注册
     */
    REGISTERED_MOBILE(HttpStatus.BAD_REQUEST, 10008, "the mobile has been registered"),

    /**
     * 邮箱已被注册
     */
    REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, 10009, "the email has been registered"),

    /**
     * 业务逻辑错误
     */
    BUSINESS_LOGIC_ERROR(HttpStatus.BAD_REQUEST, 10010, "business logic error, please contact the system admin"),

    /**
     * 验证码错误
     */
    WRONG_VERIFY_CODE(HttpStatus.BAD_REQUEST, 10011, "wrong verify code"),

    /**
     * 重复操作
     */
    DUPLICATE_OPERATION(HttpStatus.BAD_REQUEST, 10012, "duplicate operation"),

    /**
     * 重复创建账号
     */
    CREATE_DUPLICATE_ACCOUNT(HttpStatus.BAD_REQUEST, 10013, "create duplicate account"),

    /**
     * 重复创建客户端用户
     */
    CREATE_DUPLICATE_CLIENT_USER(HttpStatus.BAD_REQUEST, 10014, "create duplicate client user"),


    // ------------------------------------------- unauthorized(401): 20000 -------------------------------------------
    /**
     * 未授权，也就是令牌已过期
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 20000, "the request is unauthorized"),

    /**
     * 令牌类型不匹配
     */
    MISMATCHED_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, 20001, "mismatched token type"),


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

    /**
     * 不存在的用户
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40004, "user not found"),

    ACCOUNT_NOT_FOUNT(HttpStatus.NOT_FOUND, 40005, "account not found"),


    // ----------------------------------------- internal error(500): 50000 ----------------------------------------
    /**
     * 配置项错误
     */
    WRONG_CONFIGURATION_ITEM(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "wrong configuration item"),

    /**
     * 并发错误，通常是获取所失败
     */
    CONCURRENCY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "concurrency error");


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
