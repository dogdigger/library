package com.elias.exception;

import org.springframework.http.HttpStatus;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:45 下午</p>
 * <p>description: </p>
 */
public class RestException extends RuntimeException {

    private final ErrorCode errorCode;

    public RestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String errMessage) {
        this.errorCode = errorCode;
        this.errorCode.setErrorMessage(errMessage);
    }

    @Override
    public String getMessage() {
        return errorCode.getErrorMessage();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

    public String getCode() {
        return errorCode.getHttpStatus().value() + String.valueOf(errorCode.getBusinessCode());
    }

    public void setMessage(String message){
        this.errorCode.setErrorMessage(message);
    }
}
