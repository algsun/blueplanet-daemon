package com.microwise.msp.util;

import com.google.common.base.Strings;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author gaohui
 * @date 13-4-27 10:54
 */
public class DateTimeUtil {

    //==========以下为 blackhole 用到
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String HH_MM = "HH:mm";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY = "yyyy";

    private final static SimpleDateFormat FULL_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    private final static SimpleDateFormat FULL_FORMAT_NO_SECOND = new SimpleDateFormat(YYYY_MM_DD_HH_MM);
    private final static SimpleDateFormat Y_M_D_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
    private final static SimpleDateFormat H_M_FORMAT = new SimpleDateFormat(HH_MM);
    private final static SimpleDateFormat M_D_H_M_FORMAT = new SimpleDateFormat(MM_DD_HH_MM);

    /**
     * 返回 "yyyy-MM-dd HH:mm:ss" 格式字符串
     *
     * @param date
     * @return
     */
    public static String formatFull(Date date) {
        return FULL_FORMAT.format(date);
    }

    /**
     * 根据 "yyyy-MM-dd HH:mm:ss" 解析, 返回 Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseFull(String dateStr) {
        return parseUncheck(FULL_FORMAT, dateStr);
    }

    /**
     * 解析时间，如果有异常, 换为免检异常
     *
     * @param df
     * @param dateStr
     * @return
     * @throw IllegalArgumentException if ParseException
     */
    public static Date parseUncheck(DateFormat df, String dateStr) {
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 以指定的方式格式化时间
     *
     * @param format 格式
     * @param date   时间
     * @return
     */
    public static String format(String format, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 以指定的方式解析时间
     *
     * @param format  格式
     * @param dateStr 时间字符串
     */
    public static Date parse(String format, String dateStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(dateStr);
    }

    /**
     * 解析时间，如果有异常, 换为免检异常
     *
     * @param format
     * @param dateStr
     * @return
     * @throw IllegalArgumentException if ParseException
     */
    public static Date parseUncheck(String format, String dateStr) {
        try {
            return new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //==========以下为 orion 用到

    /**
     * 出库事件编号
     *
     * @param siteId 站点编号   z
     * @return 出库事件编号
     */
    public static String getOutEventId(String siteId) {
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        return siteId + df.format(new Date());
    }

    //==========以下为 phoenix 用到

    /**
     * 将 毫秒 转换为分钟 ，少于一分钟钟按一分钟计算
     *
     * @param time
     * @return
     * @author xu.baoji
     * @date 2013-7-15
     */
    public static Long millisecondToMinute(Long time) {
        long a = time % 60000;
        long b = time / 60000;
        return a == 0 ? b : b + 1;
    }

    /**
     * 将 分种转换为小时 取 两位小数
     *
     * @param time
     * @return
     * @author xu.baoji
     * @date 2013-7-15
     */
    public static Float minuteSecondToHour(Long time) {
        // 将分钟转换为小时
        Float a = time.floatValue() / 60;
        // 四舍五入 取两位小数
        BigDecimal bd = new BigDecimal(a);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 毫秒 to 小时
     *
     * @param time
     * @return
     * @author xu.baoji
     * @date 2013-7-22
     */
    public static Float millisecondToHour(Long time) {
        return minuteSecondToHour(millisecondToMinute(time));
    }

    /**
     * 将时间字符串转成Date类型，若时间为空，则返回当前时间
     *
     * @param time
     * @return
     * @author wang.geng
     * @date 2013-8-14
     */
    public static Date stringToDate(String time) {
        Date date = null;
        if (Strings.isNullOrEmpty(time)) {
            date = new Date();
        } else {
            // 日期格式化
            date = DateTime.parse(time).toDate();
        }
        return date;
    }

    //==========以下为 proxima 用到

    /**
     * 返回 "yyyy-MM-dd HH:mm" 格式字符串
     *
     * @param date
     * @return
     */
    public static String formatFullNoSecond(Date date) {
        return FULL_FORMAT_NO_SECOND.format(date);
    }

    /**
     * 根据 "yyyy-MM-dd HH:mm" 解析, 返回 Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseFullNoSecond(String dateStr) {
        return parseUncheck(FULL_FORMAT_NO_SECOND, dateStr);
    }

    /**
     * 转换为时分
     *
     * @param time
     * @return
     */
    public static String formatTime(Date time) {
        return H_M_FORMAT.format(time);
    }

    /**
     * 解析时分. 格式为 "HH:mm"
     *
     * @param timeStr 时间字符串
     * @return
     */
    public static Date parseTime(String timeStr) throws ParseException {
        return parseUncheck(H_M_FORMAT, timeStr);
    }

    /**
     * 转换为月日时分(MM-dd HH:mm)
     *
     * @param time
     * @return
     */
    public static String formatDateHour(Date time) {
        return M_D_H_M_FORMAT.format(time);
    }
    //==========common.util.GalaxyDateUtil

    /**
     * 格式化时间为指定格式
     *
     * @param formatTemple 格式化模版：如yyyy-MM-dd
     * @param date         时间
     * @return Date
     * @author zhangpeng
     * @date 2013-3-8
     */
    public static Date dateFormatReDate(String formatTemple, Date date) {
        try {
            SimpleDateFormat sim = new SimpleDateFormat(formatTemple);
            String time = sim.format(date);
            DateTime dt = new DateTime(sim.parse(time));
            return dt.toDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化时间为指定格式
     *
     * @param formatTemple 格式化模版：如yyyy-MM-dd
     * @param date         时间
     * @return String
     * @author zhangpeng
     * @date 2013-3-8
     */
    public static String dateFormatReString(String formatTemple, Date date) {
        SimpleDateFormat sim = new SimpleDateFormat(formatTemple);
        return sim.format(date);
    }
    //=============================日期工具类汇总，以上是原其它业务系统中的日期工具类

    /**
     * 一年的开始
     *
     * @param date
     * @return
     */
    public static Date startOfYear(Date date) {
        return startOfYearDT(date).toDate();
    }

    /**
     * 一年的结束
     *
     * @param date
     * @return
     */
    public static Date endOfYear(Date date) {
        return endOfYearDT(date).toDate();
    }

    /**
     * 一年的开始
     *
     * @param date
     * @return
     */
    public static DateTime startOfYearDT(Date date) {
        DateTime dateTime = DateTime.now().withMillis(date.getTime())
                .monthOfYear().withMinimumValue();
        return startOfMonthDT(dateTime.toDate());
    }

    /**
     * 一年的结束
     *
     * @param date
     * @return
     */
    public static DateTime endOfYearDT(Date date) {
        DateTime dateTime = DateTime.now().withMillis(date.getTime())
                .monthOfYear().withMaximumValue();
        return endOfMonthDT(dateTime.toDate());
    }

    /**
     * 当前月的开始
     *
     * @param date
     * @return
     */
    public static Date startOfMonth(Date date) {
        return startOfMonthDT(date).toDate();
    }

    /**
     * 当前月的结束
     *
     * @param date
     * @return
     */
    public static Date endOfMonth(Date date) {
        return endOfMonthDT(date).toDate();
    }

    /**
     * 当前月的开始
     *
     * @param date
     * @return
     */
    public static DateTime startOfMonthDT(Date date) {
        DateTime dateTime = DateTime.now().withMillis(date.getTime())
                .dayOfMonth().withMinimumValue();
        return startOfDayDT(dateTime.toDate());
    }

    /**
     * 当前月的结束
     *
     * @param date
     * @return
     */
    public static DateTime endOfMonthDT(Date date) {
        DateTime dateTime = DateTime.now().withMillis(date.getTime())
                .dayOfMonth().withMaximumValue();
        return endOfDayDT(dateTime.toDate());
    }

    /**
     * 当天的开始
     *
     * @param date
     * @return
     */
    public static Date startOfDay(Date date) {
        return startOfDayDT(date).toDate();
    }

    /**
     * 当天的结束
     *
     * @param date
     * @return
     */
    public static Date endOfDay(Date date) {
        return endOfDayDT(date).toDate();
    }

    /**
     * 当天的开始
     *
     * @param date
     * @return
     */
    public static DateTime startOfDayDT(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .hourOfDay().withMinimumValue()
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue()
                .millisOfSecond().withMinimumValue();
    }

    /**
     * 当天的结束
     *
     * @param date
     * @return
     */
    public static DateTime endOfDayDT(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .hourOfDay().withMaximumValue()
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue()
                .millisOfSecond().withMaximumValue();
    }

    public static Date startOfHour(Date date) {
        return startOfHourDT(date).toDate();
    }

    public static Date endOfHour(Date date){
        return endOfHourDT(date).toDate();
    }

    public static DateTime startOfHourDT(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .minuteOfHour().withMinimumValue()
                .secondOfMinute().withMinimumValue()
                .millisOfSecond().withMinimumValue();
    }

    public static DateTime endOfHourDT(Date date) {
        return DateTime.now().withMillis(date.getTime())
                .minuteOfHour().withMaximumValue()
                .secondOfMinute().withMaximumValue()
                .millisOfSecond().withMaximumValue();
    }

    public static Date convertDate(String strDate, int dateType) throws ParseException {
        Date date = new Date();
        switch (dateType) {
            case 1:
                date = parse(YYYY, strDate);
                break;
            case 2:
                date = parse(YYYY_MM, strDate);
                break;
            case 3:
                date = parse(YYYY_MM_DD, strDate);
                break;
        }
        return date;
    }
}
