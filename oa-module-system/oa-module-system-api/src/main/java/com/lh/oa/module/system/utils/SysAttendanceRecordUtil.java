package com.lh.oa.module.system.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tanghanlin
 * @since 2023/11/9
 */
public class SysAttendanceRecordUtil {

    /**
     * 根据用户的项目打卡记录，获取指定时间的所属项目id
     *
     * @param projectIdAndRecordDateMap 项目和最近两个月的打卡时间映射关系
     * @param queryDate                 查询时间
     * @return 归属项目id
     */
    public static int getFinalProjectIdByAttendanceRecordMap(Map<Integer, List<Long>> projectIdAndRecordDateMap, Date queryDate) {
        if (Objects.isNull(queryDate)) {
            return 0;
        }
        // 最小差距时间和项目id都需要计算
        int beforeFinalProjectId = 0;
        int afterFinalProjectId = 0;
        long minBeforeGapTimeSecond = 0;
        long minAfterGapTimeSecond = 0;
        long queryTimestamp = queryDate.getTime();
        for (Map.Entry<Integer, List<Long>> map : projectIdAndRecordDateMap.entrySet()) {
            List<Long> recordTimestampList = map.getValue();
            // 查询时间之前最近的一次打卡记录
            Long beforeTimestamp = recordTimestampList.stream()
                    .filter(recordTimestamp -> queryTimestamp >= recordTimestamp)
                    .max(Long::compareTo)
                    .orElse(0L);
            // 查询时间之后最近的一次打卡记录
            Long afterTimestamp = recordTimestampList.stream()
                    .filter(recordTimestamp -> queryTimestamp <= recordTimestamp)
                    .min(Long::compareTo)
                    .orElse(0L);
            long beforeGapSecond = Math.abs(queryTimestamp - beforeTimestamp);
            long afterGapSecond = Math.abs(queryTimestamp - afterTimestamp);
            // 初始化
            if (Objects.equals(0L, minBeforeGapTimeSecond)) {
                minBeforeGapTimeSecond = beforeGapSecond;
                beforeFinalProjectId = map.getKey();
            }
            if (Objects.equals(0L, minAfterGapTimeSecond)) {
                minAfterGapTimeSecond = afterGapSecond;
                afterFinalProjectId = map.getKey();
            }
            if (beforeGapSecond < minBeforeGapTimeSecond) {
                minBeforeGapTimeSecond = beforeGapSecond;
                beforeFinalProjectId = map.getKey();
            }
            if (afterGapSecond < minAfterGapTimeSecond) {
                minAfterGapTimeSecond = afterGapSecond;
                afterFinalProjectId = map.getKey();
            }
        }
        // 优先取查询时间之前的记录，如果没有再取查询时间之后的，确保每个查询时间都有一个离它最近的归属项目
        return beforeFinalProjectId == 0 ? afterFinalProjectId : beforeFinalProjectId;
    }

}
