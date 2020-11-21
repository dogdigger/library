package com.elias.response;

import lombok.Data;

/**
 * @author chengrui
 * <p>create at: 2020/11/18 5:27 下午</p>
 * <p>description: </p>
 */
@Data
public class GenericResponse<T> {
    private final String code;
    private final String message;
    private T data = null;

    public GenericResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public GenericResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public GenericResponse(T data) {
        this("200", "ok", data);
    }
}
