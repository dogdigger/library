package com.elias.common.exception;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 3:47 下午</p>
 * <p>description: </p>
 */
public class SpringContextNotInitializedException extends RuntimeException {
    public SpringContextNotInitializedException(String message) {
        super(message);
    }
}
