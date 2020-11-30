package com.elias.common.exception;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 2:38 下午</p>
 * <p>description: 请求头不合法</p>
 */
public class BadRequestHeaderException extends RuntimeException {
    public BadRequestHeaderException(String message) {
        super(message);
    }
}
