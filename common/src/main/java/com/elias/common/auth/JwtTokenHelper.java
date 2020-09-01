package com.elias.common.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.elias.common.util.DateUtils;

import java.util.Date;

/**
 * @author chengrui
 * <p>create at: 2020-07-31 17:47</p>
 * <p>description: </p>
 */
public final class JwtTokenHelper {
    private static final Algorithm ALGORITHM = Algorithm.HMAC384("elias-secure-jwt");
    private static final String ISSUER = "elias-jwt";
    private static final int EXPIRES = 120;
    // 考虑到集群中的各个服务器的时间可能会有差异，因此这里设置一个2分钟的窗口期
    private static final int LEEWAY = 120;

    private JwtTokenHelper(){}

    public static String createJWT(){
        Date dt = DateUtils.now();
        return JWT.create().withIssuedAt(dt).withIssuer(ISSUER).withNotBefore(dt).withExpiresAt(DateUtils.minuteAfter(dt,EXPIRES)).sign(ALGORITHM);
    }

    public static boolean validate(String jwt){
        try{
            JWT.require(ALGORITHM).acceptLeeway(LEEWAY).withIssuer(ISSUER).build().verify(jwt);
            return true;
        } catch(JWTVerificationException e){
            return false;
        }
    }
}
