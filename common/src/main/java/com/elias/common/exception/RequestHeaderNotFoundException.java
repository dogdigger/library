package com.elias.common.exception;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 2:37 下午</p>
 * <p>description: 请求头缺失异常</p>
 */
public class RequestHeaderNotFoundException extends RuntimeException {

    public RequestHeaderNotFoundException(String headerName) {
        super("request header `" + headerName + "` is not present");
    }
}
