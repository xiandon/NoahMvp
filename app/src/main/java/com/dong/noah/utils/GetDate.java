package com.dong.noah.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class GetDate {
    /**
     * 获取Linux时间戳
     *
     * @return long类型的时间戳
     */
    public static long getStamp() {
        return System.currentTimeMillis();
    }


    public static String getStampSecond() {
        return (System.currentTimeMillis() / 1000) + "";
    }


    /**
     * 获取当前时间
     *
     * @return 时间格式的时间
     */
    public static String getDate() {
        return stampToDate(getStamp());
    }

    /**
     * 将时间转换为时间戳
     *
     * @param time 时间
     * @return 时间戳
     * @throws ParseException 异常
     */
    public static long dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        return date.getTime();
    }

    /**
     * 将时间戳转换为时间
     *
     * @param timeMillis 时间戳
     * @return 时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
}
