package com.elias.common.exception;

/**
 * @author chengrui
 * <p>create at: 2020/8/4 7:54 下午</p>
 * <p>description: 业务异常</p>
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message){
        super(message);
    }
}
