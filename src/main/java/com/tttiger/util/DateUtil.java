package com.tttiger.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2019/11/12 12:14
 */
public class DateUtil {

    /**
     * 日期类型格式 （yyyy-MM-dd）
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期时间类型格式 （yyyy-MM-dd HH:mm:ss）
     */
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 尝试将日期类型格式化给字符串返回 默认转化格式(yyyy-MM-dd)
     *
     * @param date 日期实例
     * @return 格式化字符串
     */
    public static String date2Str(@Nonnull Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_PATTERN);
        return df.format(date);
    }

    /**
     * 尝试将日期类型格式化字符串返回 默认转化格式(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 日期实例
     * @return 格式化字符串
     */
    public static String dateTime2Str(@Nonnull Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
        return df.format(date);
    }


    /**
     * @param pattern 指定转化格式
     * @param date    日期实例
     * @return 格式化字符串
     */
    public static String date2Str(@Nonnull String pattern, @Nonnull Date date) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * @param pattern 转化格式
     * @param strDate 日期字符串
     * @return 解析后的日期实例
     * 日期字符串解析异常返回 null
     */
    public static Optional<Date> str2Date(@Nonnull String pattern, @Nullable String strDate) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return Optional.ofNullable(df.parse(strDate));
        } catch (ParseException pe) {
            return Optional.empty();
        }
    }

    /**
     * @param strDate 日期字符串，格式化 yyyy-MM-dd
     * @return 解析日期
     */
    public static Optional<Date> str2Date(@Nullable String strDate) {
        return str2Date(DateUtil.DATE_PATTERN, strDate);
    }

    /**
     * @param strDate 日期字符串，格式化 yyyy-MM-dd
     * @return 解析日期
     */
    public static Optional<Date> str2DateTime(@Nullable String strDate) {
        return str2Date(DateUtil.DATETIME_PATTERN, strDate);
    }


    /**
     * 获取当前时间的格式化字符串
     *
     * @return 当前时间格式化字符串
     */
    public static String now() {
        return date2Str(DateUtil.DATETIME_PATTERN, new Date());
    }


    /**
     * @param pattern 日期字符串解析格式
     * @param strDate 日期字符串
     * @return 时间戳
     * 日期格式转换成时间戳
     */
    public static Optional<Long> getTimeStamp(String pattern, String strDate) {
        Optional<Date> dateOptional = str2Date(pattern, strDate);
        return dateOptional.map(Date::getTime);
    }


    /**
     * 判断startDate 是否是 endDate之前的时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return startDate是否是endDate之前, 解析异常返回false
     */
    public static boolean dateOneIsContainDateTwo(@Nullable String startDate, @Nullable String endDate,
                                                  @Nonnull String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        if (null == startDate || "".equals(startDate)) {
            return false;
        }
        if (null == endDate || "".equals(endDate)) {
            return false;
        }
        Date begin = null;
        Date end = null;
        try {
            begin = df.parse(startDate);
            end = df.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return !begin.after(end);
    }


    /**
     * 比较两个日期的年份是否一致
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 日期年份是否一致
     */
    public static boolean compareYear(String beginDate, String endDate) {
        Date begin = null;
        Date end = null;
        DateFormat df = new SimpleDateFormat("yyyy");
        if (null == beginDate || "".equals(beginDate)) {
            return false;
        }
        if (null == endDate || "".equals(endDate)) {
            return false;
        }
        try {
            begin = df.parse(beginDate);
            end = df.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (begin != null && end != null && begin.compareTo(end) == 0);
    }

    /**
     * 判断一个日期是否在指定日期范围之内
     *
     * @param now   中间的时间
     * @param start 开始时间
     * @param end   结束时间
     * @return now是否在start与end日期之间
     */
    public static boolean nowBeIncluded(String now, String start, String end, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date nowTime = null;
        Date dateBegin = null;
        Date dateEnd = null;
        try {
            nowTime = sdf.parse(now);
            dateBegin = sdf.parse(start);
            dateEnd = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nowBeIncluded(nowTime, dateBegin, dateEnd);
    }

    /**
     * 判断一个日期是否在指定日期范围之内
     *
     * @param now   中间的时间
     * @param start 开始时间
     * @param end   结束时间
     * @return now是否在start与end日期之间
     */
    public static boolean nowBeIncluded(Date now, Date start, Date end) {
        if (now == null || start == null || end == null) {
            return false;
        }
        return (now.getTime() >= start.getTime() &&
                now.getTime() <= end.getTime());
    }

    private DateUtil() {
    }

}
