package com.elias.common.exception;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 5:35 下午</p>
 * <p>description: common模块通用的异常</p>
 */
public class CommonModuleException extends RuntimeException {
    public CommonModuleException(String message) {
        super(message);
    }
}
