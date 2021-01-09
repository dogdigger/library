package com.elias.util;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author chengrui
 * <p>create at: 2020/11/26 11:34 上午</p>
 * <p>description: </p>
 */
public final class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 字母表，存储a-zA-Z0-9等62个字母
     */
    private static final char[] CHARS = new char[62];

    static {
        char ch1 = 'a', ch2 = 'Z', ch3 = '0';
        int pos = 0;
        while (ch1 <= 'z') {
            CHARS[pos++] = ch1++;
            CHARS[pos++] = ch2--;
        }
        while (ch3 <= '9') {
            CHARS[pos++] = ch3++;
        }
    }

    /**
     * 生成指定长度的由数字组成的随机字符串
     *
     * @param len 生成的数字字符串的长度
     * @return 随机数字字符串
     */
    public static String generateRandomNumberString(int len) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 随机生成指定长度的字符串(由字母和数字组成)
     *
     * @param len 生成的字符串的长度
     * @return 随机字符串
     */
    public static String generateRandomString(int len) {
        StringBuilder builder = new StringBuilder(len);
        int bound = CHARS.length;
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            builder.append(CHARS[random.nextInt(bound)]);
        }
        return builder.toString();
    }

    /**
     * 日期运算
     *
     * @param dt         指定日期
     * @param timeUnit   单位
     * @param unitAmount 数量
     * @return 结果
     */
    public static Date dateAdd(Date dt, TimeUnit timeUnit, int unitAmount) {
        return new Date(dt.getTime() + timeUnit.toMillis(unitAmount));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateRandomNumberString(6));
        }
        System.out.println("================");
        for (int i = 0; i < 10; i++) {
            System.out.println(generateRandomString(10));
        }
        System.out.println("================");
//        Random random = new Random();
//        for (int i = 0; i < 100; i++) {
//            System.out.println(random.nextInt(60));
//        }
    }
}
