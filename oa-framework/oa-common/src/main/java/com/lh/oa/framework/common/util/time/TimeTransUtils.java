package com.lh.oa.framework.common.util.time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 时间单位装换工具类
 *
 * @author tanghanlin
 * @since 2023/10/28
 */
public class TimeTransUtils {

    /**
     * 将时间从以小时为单位转换为以工作日为单位，暂时以8小时工作制计算
     *
     * @param hours 小时为单位的时间
     * @return 以天为单位的时间
     */
    public static BigDecimal transHours2WorkDays(BigDecimal hours) {
        return hours.divide(new BigDecimal(8), 1, RoundingMode.HALF_UP);
    }

    /**
     * 将毫秒级时间戳转为秒级时间戳
     *
     * @param milliTimestamp 毫秒级时间戳
     * @return 秒级时间戳
     */
    public static long transMilliTimestamp2SecondTimestamp(long milliTimestamp) {
        return new BigDecimal(milliTimestamp).divide(new BigDecimal(1000), 0 ,RoundingMode.DOWN).longValue();
    }

    /**
     * 将秒级时间戳转为日期
     *
     * @param secondTimestamp 秒级时间戳
     * @return 日期
     */
    public static Date transSecondTimestamp2Date(long secondTimestamp) {
        return new Date(Long.parseLong(secondTimestamp + "000"));
    }

}
