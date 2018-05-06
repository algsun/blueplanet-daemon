package com.microwise.msp.util;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author 谢登 Copyright 2011 MicroWise System Co.,Ltd.
 * @Date:2011-2-26
 */

public class DateUtils {

    /**
     * 年月日时分秒模式
     */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 转换时间的类型
     *
     * @return
     */
    public static Date getDate(String fromDate) {
        return getDate(fromDate, DATE_TIME_PATTERN);
    }

    /**
     * 转换时间的类型
     *
     * @return
     */
    public static Date getDate(String fromDate, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 转换时间的类型
     *
     * @return
     */
    public static Date convertDate(Date fromDate, String pattern) {
        String tmp = getDate(fromDate, pattern);
        Date date = getDate(tmp, pattern);
        return date;
    }

    /**
     * 转换时间的类型
     *
     * @return
     */
    public static String getDate(Date fromDate) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        String date = dateFormat.format(fromDate);
        return date;
    }

    /**
     * 转换时间的类型
     *
     * @return
     */
    public static String getDate(Date fromDate, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String date = dateFormat.format(fromDate);
        return date;
    }

    /**
     * 在当前的日期上增加天数
     *
     * @param date      传入的日期参数
     * @param addDayNum 在当前日期上添加或减少的天数
     * @return
     */
    public static String changeDate(Date date, int addDayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, addDayNum);
        date = calendar.getTime();
        return DateUtils.getDate(date, DATE_TIME_PATTERN);
    }

    /**
     * 在当前的日期上增加小时
     *
     * @param date       传入的日期参数
     * @param addHourNum 在当前日期上添加或减少的天数
     * @return
     */
    public static Date addHour(Date date, int addHourNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, addHourNum);
        date = calendar.getTime();
        return date;
    }

    /**
     * 在当前的日期上增加天数
     *
     * @param date      传入的日期参数
     * @param addDayNum 在当前日期上添加或减少的天数
     * @return
     */
    public static Date addDay(Date date, int addDayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, addDayNum);
        date = calendar.getTime();
        return date;
    }

    /**
     * 在当前的日期上增加天数
     *
     * @param date      传入的日期参数
     * @param addDayNum 在当前日期上添加或减少的天数
     * @return
     */
    public static String changeDate(String date, int addDayNum) {
        Date dt = DateUtils.getDate(date);
        return changeDate(dt, addDayNum);
    }

    public static Date startOfDay(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .hourOfDay().withMinimumValue()
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue()
                .millisOfSecond().withMinimumValue().toDate();
    }

    public static Date endOfDay(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .hourOfDay().withMaximumValue()
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue()
                .millisOfSecond().withMaximumValue().toDate();
    }

    public static Date startOfHour(Date date) {
        return new DateTime(date).minuteOfHour().
                withMinimumValue().secondOfMinute().
                withMinimumValue().millisOfSecond().
                withMinimumValue().toDate();
    }

    public static Date endOfHour(Date date) {
        return new DateTime(date).minuteOfHour()
                .withMaximumValue().secondOfMinute()
                .withMaximumValue().millisOfSecond()
                .withMaximumValue().toDate();
    }

}
