package com.elias.response;

import com.elias.exception.RestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chengrui
 * <p>create at: 2020/9/5 10:59 上午</p>
 * <p>description: </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private String code;
    private String message;

    public ErrorResponse(RestException restException){
        code = restException.getCode();
        message = restException.getMessage();
    }

    public ErrorResponse(RestException restException, String errMsg) {
        code = restException.getCode();
        message = errMsg;
    }
}
