package com.lh.oa.module.system.full.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lh.oa.module.system.full.constants.ISysConstant.DEFAULT_PAGEABLE_PARAM_NAME;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class Utils {

    public static final int THOUSAND = 1000;

    public static int unixTime() {
        return (int) (currentTimeMillis() / THOUSAND);
    }

    public static int unixTime(DateTime dateTime) {
        return (int) (dateTime.getTime() / THOUSAND);
    }

    public static String withPageable(SQL sql, Pageable pageable) {
        return withPageable(sql, pageable, DEFAULT_PAGEABLE_PARAM_NAME);
    }

    public static String withPageable(SQL sql, Pageable pageable, String name) {
        if (pageable == null || pageable.isUnpaged())
            return sql.toString();

        if (pageable.getSort().isSorted()) {
            int i = 0;
            for (Sort.Order order : pageable.getSort())
                sql.ORDER_BY(format("#{%s.sort.orders[%d].property} %s", name, i++, order.getDirection().name()));
        }
        return pageable.getPageSize() == 0 ? sql.toString() : format("%s LIMIT #{%s.offset}, #{%s.pageSize}", sql.toString(), name, name);
    }

    public static String toDateTimestampShippedFormat(Long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("MM.dd").format(gc.getTime());
    }

    public static String toDateTimestampStartFormat(Long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(gc.getTime());
    }

    public static String toDateTimestampEndFormat(Long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("MM/dd HH:mm").format(gc.getTime());
    }

    public static String toDateTimeFormat(long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(gc.getTime());
    }

    public static String toDateFormat(long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime());
    }

    public static String toDateMonthFormat(long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("yyyy-MM").format(gc.getTime());
    }

    public static String toTimeFormat(long time) {
        time = time * 1000;
        Date date = new Date(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new SimpleDateFormat("HH:mm:ss").format(gc.getTime());
    }

    public static boolean afterPileNoPattern(String value) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{0,2}(\\.\\d{1,3})?$|^0(\\.\\d{1,3})?$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static int getBeforeYesterdayStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static int getCurrentWeekStartTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     *
     */
    public static int getCurrentDayStartTimeStamp() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (int) (c.getTimeInMillis() / 1000);
    }

    /**
     * 获取当月开始时间
     */
    public static int getCurrentMonthStartTimeStamp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0); //获取当前月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
        cal.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
        cal.set(Calendar.MINUTE, 0); //将分钟至0
        cal.set(Calendar.SECOND, 0); //将秒至0
        cal.set(Calendar.MILLISECOND, 0); //将毫秒至0
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static String toClassesDateTimestampFormat(Long startTime, Long endTime) {
        startTime = startTime * 1000;
        endTime = endTime * 1000;
        Date startTimeDate = new Date(startTime);
        Date endTimeDate = new Date(endTime);
        GregorianCalendar startGc = new GregorianCalendar();
        startGc.setTime(startTimeDate);

        GregorianCalendar endGc = new GregorianCalendar();
        endGc.setTime(endTimeDate);

        String startTimeString = new SimpleDateFormat("MM.dd").format(startGc.getTime()).toString();
        String endTimeString = new SimpleDateFormat("MM.dd").format(endGc.getTime()).toString();
        if (hasText(startTimeString)) {
            if (startTimeString.equalsIgnoreCase(endTimeString)) {
                return startTimeString;
            } else {
                return String.format("%s-%s", startTimeString, endTimeString);
            }
        }
        return startTimeString;
    }

    public static Date toDate(long sourceTime) {
        sourceTime = sourceTime * 1000;
        return new Date(sourceTime);
    }

    public static String getMonthNoAndWeekNo(Date date) {
        int month = DateUtil.month(date) + 1;
        int week = DateUtil.weekOfMonth(date);
        return month + "月-" + week + "周";
    }

    public static String getMonthNoAndWeekNoDateRange(Date date, Date lastDate) {
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        DateTime beginOfMonth = DateUtil.beginOfMonth(date);
        String begin;
        if (beginOfWeek.isAfterOrEquals(beginOfMonth)) {
            begin = beginOfWeek.toDateStr();
        } else {
            begin = beginOfMonth.toDateStr();
        }
        DateTime endOfWeek = DateUtil.endOfWeek(date);
        DateTime endOfMonth = DateUtil.endOfMonth(date);
        String end;
        if (endOfWeek.isBeforeOrEquals(endOfMonth)) {
            if (DateUtil.month(lastDate) == DateUtil.thisMonth() && DateUtil.weekOfMonth(lastDate) == DateUtil.weekOfMonth(date)
                    && DateUtil.date(lastDate).isBeforeOrEquals(endOfMonth)) {
                if (DateUtil.date(lastDate).isBefore(DateUtil.beginOfWeek(DateUtil.date()))) {
                    end = DateUtil.endOfWeek(lastDate).toDateStr();
                } else if (DateUtil.weekOfMonth(lastDate) == DateUtil.weekOfMonth(DateUtil.date())) {
                    end = DateUtil.date().toDateStr();
                } else {
                    end = DateUtil.date(lastDate).toDateStr();
                }
            } else {
                end = endOfWeek.toDateStr();
            }
        } else {
            end = endOfMonth.toDateStr();
        }
        return begin + "~" + end;
    }

    public static String getMonthNoDateRange(Date date, Date lastDate) {
        DateTime beginOfMonth = DateUtil.beginOfMonth(date);
        DateTime endOfMonth = DateUtil.endOfMonth(date);
        String end;
        if (DateUtil.month(lastDate) == DateUtil.thisMonth() && DateUtil.date(lastDate).isBeforeOrEquals(endOfMonth)) {
            if (DateUtil.date(lastDate).isBeforeOrEquals(DateUtil.date())) {
                end = DateUtil.date().toDateStr();
            } else {
                end = DateUtil.date(lastDate).toDateStr();
            }
        } else {
            end = endOfMonth.toDateStr();
        }
        return beginOfMonth.toDateStr() + "~" + end;
    }

}
