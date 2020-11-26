package com.elias.util;

import com.elias.config.Constants;
import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/11/24 4:29 下午</p>
 * <p>description: </p>
 */
public final class SecurityUtils {
    private static final Base64.Decoder b64Decoder = Base64.getDecoder();
    private static final Base64.Encoder b64Encoder = Base64.getEncoder();
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String decodeToBase64(String msg) {
        if (msg == null || msg.equals("")) {
            throw new IllegalArgumentException("can not decode a null or empty string");
        }
        return new String(b64Decoder.decode(msg));
    }

    public static String encodeByBase64(String msg) {
        if (msg == null || msg.equals("")) {
            throw new IllegalArgumentException("can not encode a null value");
        }
        return new String(b64Encoder.encode(msg.getBytes()));
    }

    /**
     * 使用指定的哈希算法对消息进行哈希，并将哈希的结果转换成36进制返回。如果算法名称不是规定的名称，将会抛出异常。
     * SHA3-224 produces a 224 bit digest.
     * SHA3-256 produces a 256 bit digest.
     * SHA3-384 produces a 384 bit digest.
     * SHA3-512 produces a 512 bit digest
     *
     * @param algorithm 算法名称，比如SHA-224、SHA-256
     * @param messages  需要进行哈希的内容
     * @return 消息的哈希
     */
    public static String digest(String algorithm, String... messages) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm == null ? "SHA-1" : algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("no such a digest algorithm: " + algorithm);
        }
        for (String message : messages) {
            messageDigest.update(message.getBytes());
        }
        return new BigInteger(1, messageDigest.digest()).toString(36);
    }

    /**
     * 从给定的Authorization请求头中解析出(客户端)令牌，业务需要自己调用方法验证这个令牌是否有效
     *
     * @param authorizationHeader 给定的请求头，应该是: Bearer {{clientToken}}
     * @return clientToken
     */
    public static UUID getClientToken(String authorizationHeader) {
        if (!authorizationHeader.startsWith(Constants.HEADER_AUTHORIZATION_BEARER_PREFIX)) {
            throw new RestException(ErrorCode.WRONG_AUTHORIZATION_HEADER);
        }
        String clientToken = authorizationHeader.substring(Constants.HEADER_AUTHORIZATION_BEARER_PREFIX.length());
        try {
            return UUID.fromString(clientToken);
        } catch (Exception e) {
            throw new RestException(ErrorCode.WRONG_AUTHORIZATION_HEADER);
        }
    }

    /**
     * 对指定的密码加上指定的salt进行加密
     * @param password  密码
     * @param salt  salt
     * @return  加密后的结果
     */
    public static String encrypt(String password, String salt) {
        if (salt != null && StringUtils.isEmpty(salt)) {
            password += salt;
        }
        return passwordEncoder.encode(password);
    }

    public static void main(String[] args) {
        String rawPassword = "123456", salt = "abc";
        // 对原始密码进行sha256加密
        String digest = digest("SHA-256", rawPassword);
        System.out.println("digest: " + digest);
        System.out.println("encrypt mo salt: " + passwordEncoder.encode(digest));
        // 对sha256加密后的结果加盐再加密
        String encrypt = encrypt(digest, salt);
        System.out.println("encrypt: " + encrypt);
        System.out.println(passwordEncoder.matches(digest+salt, encrypt));
    }
}
