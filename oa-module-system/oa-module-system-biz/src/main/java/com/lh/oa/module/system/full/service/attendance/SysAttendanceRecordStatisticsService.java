package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceStatisticInfoTo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tanghanlin
 * @since 2023/11/11
 */
public interface SysAttendanceRecordStatisticsService {

    /**
     * 获取用户发起的流程定义表单中的小时差距
     *
     * @param userIdAndProjectIds   用户和项目的关联关系
     * @param attendMonth           查询月份，秒级时间戳
     * @param processKey            流程标识，leave请假，add加班，travel出差
     * @param returnUnit            返回单位，DAY天，HOURS小时
     * @return 用户-项目-小时差距的映射关系
     */
    AttendanceStatisticInfoTo getUserAndProjectInstanceInfoTo(Map<Long, Set<Integer>> userIdAndProjectIds, Integer attendMonth,
                                                              String processKey, String returnUnit);

}
