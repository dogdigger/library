package com.elias.auth.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author chengrui
 * <p>create at: 2020/9/3 7:41 下午</p>
 * <p>description: </p>
 */
public final class DateUtils {
    private DateUtils() {
    }

    /**
     * 日期转换成时间戳
     *
     * @param dt 待转换的日期
     * @return 日期对应的时间戳
     */
    public static long timestamp(Date dt) {
        return dt.getTime() / 1000;
    }

    /**
     * 当前时间的时间戳
     *
     * @return 时间戳
     */
    public static long timestampNow() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 两个日期是否是同一天
     *
     * @param d1
     * @param d2
     * @return boolean
     */
    public static boolean isSameDay(Date d1, Date d2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(d1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(d2);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }
}
