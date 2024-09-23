package com.lh.oa.framework.common.util.time;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 基于LocalTime的时间操作工具类
 *
 * @author tanghanlin
 * @since 2023/10/23
 */
public class TimeUtils {

    public static final ZoneId ZONE_ID_CN = ZoneId.of("Asia/Shanghai");
    public static final LocalTime START_OF_DAY = LocalTime.of(0, 0, 0, 0);
    public static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59, 0);

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DATE = "yyyy-MM-dd";

    public static final String MONTH = "yyyy-MM";
    public static final String DATE_HOUR = "yyyy-MM-dd HH";
    public static final String DATE_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME = "HH:mm:ss";

    public static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDD).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter YYYYMM_FORMATTER = DateTimeFormatter.ofPattern(YYYYMM).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter YYYY_FORMATTER = DateTimeFormatter.ofPattern(YYYY).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter MM_FORMATTER = DateTimeFormatter.ofPattern(MM).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern(MONTH).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter DATE_HOUR_FORMATTER = DateTimeFormatter.ofPattern(DATE_HOUR).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter DATE_MINUTE_FORMATTER = DateTimeFormatter.ofPattern(DATE_MINUTE).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME).withZone(ZONE_ID_CN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME).withZone(ZONE_ID_CN);

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZONE_ID_CN);
    }

    public static long currentSecond() {
        return Instant.now().getEpochSecond();
    }

    public static long currentMilli() {
        return Instant.now().toEpochMilli();
    }

    public static String format(Date date, String format) {
        if (Objects.isNull(date)) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format).withZone(ZONE_ID_CN);
        return dateTimeFormatter.format(date.toInstant());
    }

    public static String format(Instant instant, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format).withZone(ZONE_ID_CN);
        return dateTimeFormatter.format(instant);
    }

    public static String formatAsYYYYMMDD(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return YYYYMMDD_FORMATTER.format(date.toInstant());
    }

    public static String formatAsYYYYMMDD(Instant instant) {
        return YYYYMMDD_FORMATTER.format(instant);
    }

    public static String formatAsYYYYMM(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return YYYYMM_FORMATTER.format(date.toInstant());
    }

    public static String formatAsYYYYMM(Instant instant) {
        return YYYYMM_FORMATTER.format(instant);
    }

    public static String formatAsYYYY(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return YYYY_FORMATTER.format(date.toInstant());
    }

    public static String formatAsYYYY(Instant instant) {
        return YYYY_FORMATTER.format(instant);
    }

    public static String formatAsMM(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return MM_FORMATTER.format(date.toInstant());
    }

    public static String formatAsMM(Instant instant) {
        return MM_FORMATTER.format(instant);
    }

    public static String formatAsMonth(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return MONTH_FORMATTER.format(date.toInstant());
    }

    public static String formatAsMonth(Instant instant) {
        return MONTH_FORMATTER.format(instant);
    }

    public static String formatAsDate(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return DATE_FORMATTER.format(date.toInstant());
    }

    public static String formatAsDate(Instant instant) {
        return DATE_FORMATTER.format(instant);
    }

    public static String formatAsTime(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return TIME_FORMATTER.format(date.toInstant());
    }

    public static String formatAsTime(Instant instant) {
        return TIME_FORMATTER.format(instant);
    }

    public static String formatAsHour(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return DATE_HOUR_FORMATTER.format(date.toInstant());
    }

    public static String formatAsHour(Instant instant) {
        return DATE_HOUR_FORMATTER.format(instant);
    }

    public static String formatAsDateMinute(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return DATE_MINUTE_FORMATTER.format(date.toInstant());
    }

    public static String formatAsDateMinute(Instant instant) {
        return DATE_MINUTE_FORMATTER.format(instant);
    }

    public static String formatAsDateTime(Date date) {
        if (Objects.isNull(date)) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(date.toInstant());
    }

    public static String formatAsDateTime(Instant instant) {
        return DATE_TIME_FORMATTER.format(instant);
    }

    public static String formatAsDateTime(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(localDateTime);
    }

    public static String formatAsDate(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        return DATE_FORMATTER.format(localDateTime);
    }

    public static ZonedDateTime ofDate(Date date) {
        return date.toInstant().atZone(ZONE_ID_CN);
    }

    public static ZonedDateTime ofMilli(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atZone(ZONE_ID_CN);
    }

    public static ZonedDateTime ofSecond(long epochSecond) {
        return Instant.ofEpochSecond(epochSecond).atZone(ZONE_ID_CN);
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZONE_ID_CN).toInstant());
    }

    public static Date parseAsDateTime(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return Date.from(ZonedDateTime.parse(str, DATE_TIME_FORMATTER).toInstant());
    }

    public static LocalDateTime parseAsLocalDateTime(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseAsLocalDateTimeByTimestamp(Long timestamp) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        String timeStr = timestamp.toString();
        if (timeStr.length() != 10 && timeStr.length() != 13) {
            return null;
        }
        if (timestamp.toString().length() == 10) {
            timeStr += "000";
        }
        Date dateTime = new Date(Long.parseLong(timeStr));
        return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault());
    }

    public static Date parseAsDate(String str, String pattern) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return DateUtils.parseDate(str, pattern);
        }
        catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static ZonedDateTime startOfMonth(ZonedDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(START_OF_DAY);
    }

    public static Date getMonthStartDate(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(START_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getMonthEndDate(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(END_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getWeekStartDate(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(DayOfWeek.of(1)).with(START_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getWeekEndDate(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(DayOfWeek.of(7)).with(END_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getLastMonthStartDate() {
        Instant instant = now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(START_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getLastMonthEndDate() {
        Instant instant = now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).with(END_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getDayStart(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(START_OF_DAY).toInstant();
        return Date.from(instant);
    }

    public static Date getDayEnd(Date date) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.with(END_OF_DAY).toInstant();
        return Date.from(instant);
    }

    /**
     * 本方法去掉了time部分，相当于对天做truncate
     * 例如start = 2020-09-04 23:59:59, end = 2020-09-05 00:00:00
     * 用本方法计算出来的结果是1
     */
    public static long daysBetween(Date start, Date end) {
        LocalDate startDate = ZonedDateTime.ofInstant(start.toInstant(), ZONE_ID_CN).toLocalDate();
        LocalDate endDate = ZonedDateTime.ofInstant(end.toInstant(), ZONE_ID_CN).toLocalDate();
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 支持string类型的month加法，例如输入为201912 + 5 = 202005
     */
    public static String plusMonths(String month, long monthsToAdd) {
        YearMonth yearMonth = YearMonth.parse(month, YYYYMM_FORMATTER);
        return yearMonth.plusMonths(monthsToAdd).format(YYYYMM_FORMATTER);
    }

    public static String minusMonths(String month, long monthsToMinus) {
        YearMonth yearMonth = YearMonth.parse(month, YYYYMM_FORMATTER);
        return yearMonth.minusMonths(monthsToMinus).format(YYYYMM_FORMATTER);
    }

    public static Date plusDays(Date date, long daysToAdd) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.plusDays(daysToAdd).toInstant();
        return Date.from(instant);
    }

    public static Date minusDays(Date date, long daysToMinus) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.minusDays(daysToMinus).toInstant();
        return Date.from(instant);
    }

    public static Date plusHours(Date date, long hoursToAdd) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.plusHours(hoursToAdd).toInstant();
        return Date.from(instant);
    }

    public static Date minusHours(Date date, long hoursToMinus) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.minusHours(hoursToMinus).toInstant();
        return Date.from(instant);
    }

    public static Date plusMinutes(Date date, long minutesToAdd) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.plusMinutes(minutesToAdd).toInstant();
        return Date.from(instant);
    }

    public static Date minusMinutes(Date date, long minutesToMinus) {
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(date.toInstant(), ZONE_ID_CN);
        Instant instant = dateTime.minusMinutes(minutesToMinus).toInstant();
        return Date.from(instant);
    }

    /**
     * 获取两个month之间的所有month，例如输入是201912和202005，
     * 返回值是[201912, 202001, 202002, 202003, 202004, 202005]
     */
    public static List<String> getMonthsBetween(String startMonth, String endMonth) {
        YearMonth startYearMonth = YearMonth.parse(startMonth, YYYYMM_FORMATTER);
        YearMonth endYearMonth = YearMonth.parse(endMonth, YYYYMM_FORMATTER);

        List<String> months = new ArrayList<>();
        YearMonth month = startYearMonth;
        while (!month.isAfter(endYearMonth)) {
            months.add(month.format(YYYYMM_FORMATTER));
            month = month.plusMonths(1);
        }

        return months;
    }

    public static int restDaysOfMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZONE_ID_CN).toLocalDate();
        int dayOfMonth = localDate.getDayOfMonth();
        int lengthOfMonth = localDate.lengthOfMonth();
        return lengthOfMonth - dayOfMonth + 1;
    }

    public static int lengthOfMonth(Date date) {
        return date.toInstant().atZone(ZONE_ID_CN).toLocalDate().lengthOfMonth();
    }

}
