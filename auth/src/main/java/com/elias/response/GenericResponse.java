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

    private GenericResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private GenericResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private GenericResponse(T data) {
        this("200", "ok", data);
    }

    public static GenericResponse<String> ok() {
        return new GenericResponse<String>(null);
    }

    public static <T> GenericResponse<T> ok(T data) {
        return new GenericResponse<>(data);
    }

    public static <T> GenericResponse<T> response(String code, String message) {
        return new GenericResponse<>(code, message);
    }

    public static <T> GenericResponse<T> response(String code, String message, T data) {
        return new GenericResponse<>(code, message, data);
    }
}
