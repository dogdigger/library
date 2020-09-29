package com.elias.util;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * @author chengrui
 * <p>create at: 2020/9/22 7:26 下午</p>
 * <p>description: 随机生成指定长度的appSecret，长度至少为11</p>
 */
public final class AppSecretUtils {
    private AppSecretUtils() {
    }

    private final static char[] codes = new char[64];

    static {
        int i = 0;
        codes[i++] = '_';
        for (char ch = 'a'; ch <= 'z'; ch++) {
            codes[i++] = ch;
            codes[i++] = (char) (ch - 32);
        }
        for (char ch = '0'; ch <= '9'; ch++) {
            codes[i++] = ch;
        }
        codes[i] = '-';
    }

    /**
     * 得到一个long型数字的二进制表示
     *
     * @param number long型数字
     * @return 二进制表示
     */
    private static String getBinary(long number) {
        char[] bits = new char[64];
        int i = 63;
        while (i >= 0) {
            bits[i--] = (char) ('0' + (number & 1));
            number = number >> 1;
        }
        return new String(bits);
    }

    /**
     * 计算二进制字符串所表示的整形值。不考虑符号
     *
     * @param binary 二进制字符串
     * @return 整形值
     */
    private static int getIntValueFromBinary(String binary) {
        if (binary == null) {
            throw new IllegalArgumentException("can not transform a null value into a int value");
        }
        if (binary.length() > 32) {
            throw new IllegalArgumentException("the length exceed its limit, max length is 32");
        }
        int value = 0, basePos = binary.length() - 1, pos = basePos;
        while (pos >= 0) {
            char ch = binary.charAt(pos);
            if (ch != '0' && ch != '1') {
                throw new IllegalArgumentException("the parameter is not a pure binary string");
            }
            if (ch == '1') {
                value += (1 << (basePos - pos));
            }
            pos--;
        }
        return value;
    }

    /**
     * 对一个二进制字符串进行编码
     *
     * @param binary 二进制字符串
     * @return 编码的结果
     */
    private static String encodeBinary(String binary) {
        if (binary == null) {
            throw new IllegalArgumentException("can not encode a null binary string");
        }
        char[] chs = new char[binary.length() / 6 + (binary.length() % 6 == 0 ? 0 : 1)];
        int p1 = 0, p2 = 6, i = 0;
        while (p2 <= binary.length()) {
            chs[i++] = codes[getIntValueFromBinary(binary.substring(p1, p2))];
            p1 = p2;
            p2 += 6;
        }
        if (p1 < binary.length()) {
            String s = binary.substring(p1);
            p1 = binary.length();
            char[] pad = new char[p2 - p1];
            Arrays.fill(pad, '0');
            chs[i] = codes[getIntValueFromBinary(new String(pad) + s)];
        }
        return new String(chs);
    }

    public static String generate(int len) {
        if (len < 10) {
            throw new IllegalArgumentException("len can not be smaller than 10");
        }
        Random random = new Random();
        char[] secret = new char[len];
        for (int i = 0; i < len; i++) {
            int rand = random.nextInt();
            while (rand < 0) {
                rand = random.nextInt();
            }
            secret[i] = codes[rand % codes.length];
        }
        return new String(secret);
    }

    /**
     * 将一个随机UUID进行编码得到长度为22的字符串
     *
     * @return uuid编码后的结果
     */
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return encodeBinary(getBinary(uuid.getMostSignificantBits())) + encodeBinary(getBinary(uuid.getLeastSignificantBits()));
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            System.out.println(AppKeySecretUtils.generate(20));
//        }
//        String binary = getBinary(123L);
//        System.out.println(binary);
//        System.out.println(getIntValueFromBinary("1001010"));
        System.out.println(generate());
    }
}
