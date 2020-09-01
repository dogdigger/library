package com.elias.common.util;

import java.util.Date;

/**
 * @author chengrui
 * <p>create at: 2020/8/3 7:42 下午</p>
 * <p>description: </p>
 */
public class DateUtils {
    public static Date now(){
        return new Date();
    }

    public static Date minuteAfter(Date date, int minute){
        return new Date(date.getTime() + minute * 60 * 1000);
    }
}
