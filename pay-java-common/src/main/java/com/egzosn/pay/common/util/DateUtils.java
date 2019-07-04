package com.egzosn.pay.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.Args;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期转换运算工具
 *
 * @author egan
 *         <pre>
 *         email egzosn@gmail.com
 *         date 2018-11-21 16:43:20
 *         </pre>
 */
public final class DateUtils {
    private DateUtils() {
    }

    private static final Log LOG = LogFactory.getLog(DateUtils.class);
    static final class DateFormatHolder {
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>();

        DateFormatHolder() {
        }

        public static SimpleDateFormat formatFor(String pattern) {
            SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref == null ? null : ref.get();
            if (formats == null) {
                formats = new HashMap<String, SimpleDateFormat>();
                THREADLOCAL_FORMATS.set(new SoftReference(formats));
            }

            SimpleDateFormat format =  formats.get(pattern);

            if (format == null) {
                format = new SimpleDateFormat(pattern);
                format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                ((Map) formats).put(pattern, format);
            }

            return format;
        }

        public static void clearThreadLocal() {
            THREADLOCAL_FORMATS.remove();
        }
    }

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String MMDD = "MMdd";


    public static String formatDate(Date date, String pattern) {
        Args.notNull(date, "Date");
        Args.notNull(pattern, "Pattern");
        SimpleDateFormat formatFor = DateFormatHolder.formatFor(pattern);
        return formatFor.format(date);
    }
    public static Date parseDate(String date, String pattern) {
        Args.notNull(date, "Date");
        Args.notNull(pattern, "Pattern");
        SimpleDateFormat formatFor = DateFormatHolder.formatFor(pattern);
        try {
            return formatFor.parse(date);
        } catch (ParseException e) {
            LOG.error(e);
        }
        return null;
    }
    public static Date parse(String date) {
        return parseDate(date, YYYY_MM_DD_HH_MM_SS);
    }
    public static final String format(Date date) {
        return formatDate(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static final Date parseDay(String date) {
        return parseDate(date, YYYY_MM_DD);
    }

    public static final String formatDay(Date date) {
        return formatDate(date, YYYY_MM_DD);
    }

    /**
     * 剩余分钟数
     *
     * @param date 结束点日期
     * @return 分钟数
     */
    public static final long minutesRemaining(Date date) {
        return (date.getTime() - System.currentTimeMillis()) / 1000 / 60;
    }

    /**
     * 剩余小时
     *
     * @param date 结束点日期
     * @return 小时数
     */
    public static final long remainingHours(Date date) {
        return minutesRemaining(date) / 60;
    }

    /**
     * 剩余天数
     *
     * @param date 结束点日期
     * @return 天数
     */
    public static final long remainingDays(Date date) {
        return remainingHours(date) / 24;
    }

}
