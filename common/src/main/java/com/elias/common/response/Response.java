package com.elias.common.response;


/**
 * @author chengrui
 * <p>create at: 2020-07-31 11:30</p>
 * <p>description: 定义响应</p>
 */
public class Response<T> {
    private int code;
    private String message;
    private T data;

    private Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Response(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

    public T getData(){
        return this.data;
    }

    public static <T> Response<T> ok(T data){
        ResponseStatus status = ResponseStatus.SUCCESS;
        return new Response<>(status.getCode(), status.getMessage(), data);
    }

    public static <T> Response<T> ok(){
        ResponseStatus status = ResponseStatus.SUCCESS;
        return new Response<>(status.getCode(), status.getMessage());
    }

    public static <T> Response<T> build(int code, String message){
        return new Response<>(code, message);
    }

    public static <T> Response<T> build(ResponseStatus responseStatus){
        return new Response<>(responseStatus.getCode(), responseStatus.getMessage());
    }

    public static <T> Response<T> build(int code, String message, T data){
        return new Response<>(code, message, data);
    }

}
