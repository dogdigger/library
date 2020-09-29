package com.elias.util;

import java.util.Base64;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/26 11:37 上午</p>
 * <p>description: </p>
 */
public final class Base64Utils {
    private Base64Utils(){}
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    public static String decode(String msg) {
        if (msg == null || msg.equals("")) {
            throw new IllegalArgumentException("can not decode a null or empty string");
        }
        return new String(decoder.decode(msg));
    }

    public static String encode(String msg) {
        if (msg == null || msg.equals("")) {
            throw new IllegalArgumentException("can not encode a null value");
        }
        return new String(encoder.encode(msg.getBytes()));
    }

    public static void main(String[] args) {
        String msg = UUID.randomUUID() + ":" + "123456";
        System.out.println(msg);
        String t = encode(msg);
        System.out.println("encode result: " + t);
        t = decode(t);
        System.out.println("decode result: " + t);
    }
}
