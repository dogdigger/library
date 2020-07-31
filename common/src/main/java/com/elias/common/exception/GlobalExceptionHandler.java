package com.elias.common.exception;

import com.elias.common.response.Response;
import com.elias.common.response.ResponseStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chengrui
 * <p>create at: 2020-07-31 11:17</p>
 * <p>description: 全局异常处理器</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler<T> {

    /**
     * 处理空指针异常
     * @param npe {@link java.lang.NullPointerException}
     * @return {@link com.elias.common.response.Response}
     */
    @ExceptionHandler(NullPointerException.class)
    public Response<T> nullPointerExceptionHandler(NullPointerException npe){
        return Response.build(ResponseStatus.INTERNAL_ERROR.getCode(), npe.getMessage());
    }

    /**
     * 处理请求参数缺失异常
     * @param e {@link org.springframework.web.bind.MissingServletRequestParameterException}
     * @return {@link com.elias.common.response.Response}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<T> missingServletRequestParameterException(MissingServletRequestParameterException e){
        return Response.build(ResponseStatus.INCOMPLETE_PARAMETER.getCode(), e.getMessage());
    }
}
