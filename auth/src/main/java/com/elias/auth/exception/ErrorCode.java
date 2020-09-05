package com.elias.auth.exception;

import org.springframework.http.HttpStatus;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:34 下午</p>
 * <p>description: </p>
 */
public enum ErrorCode {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 1000, "resource not found"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 1000, "the request is unauthorized"),

    FORBIDDEN(HttpStatus.FORBIDDEN, 1000, "forbidden operation"),

    /**
     * 账号已被禁用
     */
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, 1001, "disabled account"),

    /**
     * 账号已被锁定
     */
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, 1002, "your account has been locked"),

    /**
     * 参数错误
     */
    PARAM_INVALID(HttpStatus.BAD_REQUEST, 1000, "wrong parameter value"),

    /**
     * 操作警告
     */
    OPERATION_WARNING(HttpStatus.BAD_REQUEST, 1001, "dangerous operation");

    private HttpStatus httpStatus;
    private int businessCode;
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
