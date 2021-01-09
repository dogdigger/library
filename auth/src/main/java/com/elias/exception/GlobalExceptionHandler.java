package com.elias.exception;

import com.elias.response.ErrorResponse;
import com.elias.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author chengrui
 * <p>create at: 2020/9/2 7:52 下午</p>
 * <p>description: 全局异常处理器</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理{@link RestException}
     *
     * @param restException 异常对象
     * @return {@link GenericResponse<String>}
     */
    @ExceptionHandler(RestException.class)
    public GenericResponse<String> handleRestException(RestException restException) {
        return GenericResponse.response(restException.getCode(), restException.getMessage());
    }

    /**
     * 处理参数校验失败异常
     *
     * @param e {@link MethodArgumentNotValidException}
     * @return {@link GenericResponse<String>}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponse<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        RestException restException = new RestException(ErrorCode.PARAM_INVALID);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        if (!errors.isEmpty()) {
            restException.setMessage(errors.get(0).getDefaultMessage());
        }
        return GenericResponse.response(restException.getCode(), restException.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        RestException restException = new RestException(ErrorCode.PARAM_INVALID);
        restException.setMessage(e.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse(restException), restException.getHttpStatus());
    }

    /**
     * 处理请求头缺失异常
     *
     * @param e {@link MissingRequestHeaderException}
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        RestException restException = new RestException(ErrorCode.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(restException), restException.getHttpStatus());
    }
}
