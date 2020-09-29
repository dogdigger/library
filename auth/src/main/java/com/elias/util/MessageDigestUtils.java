package com.elias.util;

import com.elias.exception.ErrorCode;
import com.elias.exception.RestException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 4:23 下午</p>
 * <p>description: 消息摘要工具类</p>
 */
public final class MessageDigestUtils {
    private MessageDigestUtils() {
    }

    /**
     * 使用指定的哈希算法对消息进行哈希，并将哈希的结果转换成36进制返回。如果算法名称不是规定的名称，将会抛出异常。
     * SHA3-224 produces a 224 bit digest.
     * SHA3-256 produces a 256 bit digest.
     * SHA3-384 produces a 384 bit digest.
     * SHA3-512 produces a 512 bit digest
     *
     * @param algorithm 算法名称，比如SHA-224、SHA-256
     * @param messages 需要进行哈希的内容
     * @return 消息的哈希
     */
    public static String digest(String algorithm, String... messages) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm == null ? "SHA-1" : algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RestException(ErrorCode.PARAM_INVALID, "no such a digest algorithm: " + algorithm);
        }
        for (String message : messages) {
            messageDigest.update(message.getBytes());
        }
        return new BigInteger(1, messageDigest.digest()).toString(36);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 创建一个 MessageDigest 实例
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        // 反复调用 update() 方法输入数据
        digest.update("hello".getBytes());
        digest.update("world".getBytes());
        // 调用 digest() 方法对输入数据进行哈希
        byte[] result = digest.digest();
        // 将哈希结果转成16进制字符串
        String hash = new BigInteger(1, result).toString(36);
        System.out.println(hash);
        System.out.println(hash.length());
    }
}
