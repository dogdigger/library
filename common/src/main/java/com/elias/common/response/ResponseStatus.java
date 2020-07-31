package com.elias.common.response;

/**
 * @author chengrui
 * <p>create at: 2020-07-31 11:39</p>
 * <p>description: </p>
 */
public enum ResponseStatus {
    SUCCESS(200, "ok"),
    UNAUTHORIZED(400100, "unauthorized access"),
    BAD_PARAMETER(400111, "incorrect parameter"),
    INCOMPLETE_PARAMETER(400112, "incomplete parameter"),
    INTERNAL_ERROR(500100, "system error");

    ResponseStatus(int code, String message){
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }

//    private void setMessage(String message){
//        this.message = message;
//    }
//
//    public static ResponseStatus build(ResponseStatus responseStatus, String message){
//        responseStatus.setMessage(message);
//        return responseStatus;
//    }
}
