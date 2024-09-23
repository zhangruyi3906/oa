package com.lh.oa.module.system.full.enums.attendance;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

/**
 * @see Week
 */
public enum WeekEnum {

    MONDAY(1, "一"),
    TUESDAY(2, "二"),
    WEDNESDAY(3, "三"),
    THURSDAY(4, "四"),
    FRIDAY(5, "五"),
    SATURDAY(6, "六"),
    SUNDAY(7, "日");

    private final int idx;
    private final String val;

    WeekEnum(int idx, String val) {
        this.idx = idx;
        this.val = val;
    }

    public static String ofChinese(int idx) {
        Optional<WeekEnum> optional = Arrays.stream(WeekEnum.values()).filter(w -> w.idx == idx).findFirst();
        return optional.map(weekEnum -> weekEnum.val).orElse(null);
    }

    public static boolean contain(int[] workWeeks, WeekEnum weekEnum) {
        if (weekEnum == null) {
            return false;
        }
        return ArrayUtil.contains(workWeeks, weekEnum.idx);
    }

    public static WeekEnum of(String onlyDate) {
        Week week = LocalDateTimeUtil.dayOfWeek(LocalDateTimeUtil.parseDate(onlyDate));
        switch (week.getValue()) {
            case Calendar.SUNDAY:
                return SUNDAY;
            case Calendar.MONDAY:
                return MONDAY;
            case Calendar.TUESDAY:
                return TUESDAY;
            case Calendar.WEDNESDAY:
                return WEDNESDAY;
            case Calendar.THURSDAY:
                return THURSDAY;
            case Calendar.FRIDAY:
                return FRIDAY;
            case Calendar.SATURDAY:
                return SATURDAY;
            default:
                return null;
        }
    }

    public static WeekEnum of(long onlyDate) {
        Week week = LocalDateTimeUtil.dayOfWeek(LocalDateTimeUtil.of(onlyDate).toLocalDate());
        switch (week.getValue()) {
            case Calendar.SUNDAY:
                return SUNDAY;
            case Calendar.MONDAY:
                return MONDAY;
            case Calendar.TUESDAY:
                return TUESDAY;
            case Calendar.WEDNESDAY:
                return WEDNESDAY;
            case Calendar.THURSDAY:
                return THURSDAY;
            case Calendar.FRIDAY:
                return FRIDAY;
            case Calendar.SATURDAY:
                return SATURDAY;
            default:
                return null;
        }
    }

}
