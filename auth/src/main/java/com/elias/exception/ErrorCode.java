package com.elias.exception;

import org.springframework.http.HttpStatus;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:34 下午</p>
 * <p>description: </p>
 */
public enum ErrorCode {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 1000, "resource can not be found"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1001, "no such user"),

    /**
     * 客户端不存在
     */
    CLIENT_NOT_FOUND(HttpStatus.NOT_FOUND, 1002, "no such client"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 1000, "the request is unauthorized"),

    FORBIDDEN(HttpStatus.FORBIDDEN, 1000, "forbidden operation"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(HttpStatus.FORBIDDEN, 1001, "the user has been disabled"),

    /**
     * 账号已被锁定
     */
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, 1002, "your account has been locked"),


    // ------------------------------------------- bad request -------------------------------------------
    /**
     * 参数错误
     */
    PARAM_INVALID(HttpStatus.BAD_REQUEST, 1000, "param invalid"),

    /**
     * 重复的客户端名称
     */
    DUPLICATE_CLIENT_NAME(HttpStatus.BAD_REQUEST, 1001, "duplicate client name"),



    /**
     * 操作警告
     */
    OPERATION_WARNING(HttpStatus.BAD_REQUEST, 1001, "dangerous operation"),

    /**
     * 密码错误
     */
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 1002, "wrong password");

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
