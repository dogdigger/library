package com.elias.validator;

import com.elias.common.exception.BadRequestHeaderException;
import com.elias.common.exception.RequestHeaderNotFoundException;
import com.elias.common.Constants;

import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/28 10:37 上午</p>
 * <p>description: </p>
 */
public final class AuthorizationHeaderUtils {
    private AuthorizationHeaderUtils(){}

    public static UUID validate(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new RequestHeaderNotFoundException(Constants.HEADER_AUTHORIZATION);
        }
        if (!authorizationHeader.startsWith(Constants.HEADER_AUTHORIZATION_BEARER_PREFIX)) {
            throw new BadRequestHeaderException("request header `Authorization` is illegal");
        }
        try {
            return UUID.fromString(authorizationHeader.substring(Constants.HEADER_AUTHORIZATION_BEARER_PREFIX.length()));
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BadRequestHeaderException("request header `Authorization` is illegal");
        }
    }
}
