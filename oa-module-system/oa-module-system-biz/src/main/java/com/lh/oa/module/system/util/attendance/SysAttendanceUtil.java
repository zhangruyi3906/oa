package com.lh.oa.module.system.util.attendance;

import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.enums.sysAttendance.SysAttendanceRuleHolidayTypeEnum;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import com.lh.oa.module.system.full.enums.attendance.LegalHolidayStateEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rick
 * @since 2024/2/22
 */
public class SysAttendanceUtil {

    /**
     * 根据考勤规则获取工作日列表
     *
     * @param dayList  本月的天数
     * @param rule     考勤规则
     * @param holidays 节假日列表
     * @param startDay 开始日期，一般是被查询月份影响
     * @param endDay   结束日期，一般是被入职时间影响
     * @return 工作日列表
     */
    public static List<HolidayInfoDO> getWorkDayList(List<HolidayInfoDO> dayList, SysAttendanceRuleEntity rule,
                                              List<SysAttendanceRuleHoliday> holidays, Date startDay, Date endDay) {
        // 获取考勤天数
        List<Integer> weekDayList = Arrays.stream(rule.getWeekday().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        // 获取节假日同步状态
        boolean syncHoliday = Objects.equals(LegalHolidayStateEnum.SYNC_HOLIDAY, rule.getLegalHolidayState());
        Map<Date, SysAttendanceRuleHoliday> holidayMap = CollectionUtils.isEmpty(holidays)
                ? Collections.emptyMap()
                : holidays.stream().collect(Collectors.toMap(SysAttendanceRuleHoliday::getHolidayDate, Function.identity()));

        return dayList.stream().filter(day -> {
            Date dayDate = TimeUtils.parseAsDate(day.getDate().toString(), "yyyyMMdd");
            if (Objects.isNull(dayDate)) {
                return false;
            }
            boolean rangeFlag = true;
            if (Objects.nonNull(startDay) && Objects.nonNull(endDay)) {
                rangeFlag = startDay.getTime() <= dayDate.getTime() && dayDate.getTime() <= endDay.getTime();
            }

            boolean workDayFlag;
            if (syncHoliday) {
                workDayFlag = (weekDayList.contains(day.getWeek()) || !Objects.equals(10, day.getHolidayOvertime()))
                        && Objects.equals(2, day.getHolidayRecess());
            } else {
                // 如果有自定义的休息日或者工作日，需要优先计算
                if (Objects.equals(true, rule.getEnableCustomHoliday()) && holidayMap.containsKey(dayDate)) {
                    SysAttendanceRuleHoliday customHolidayDate = holidayMap.get(dayDate);
                    workDayFlag = Objects.equals(SysAttendanceRuleHolidayTypeEnum.WORK.getCode(), customHolidayDate.getHolidayType());
                }
                // 如果也没有自定义节假日，则按勾选的工作日计算
                else {
                    workDayFlag = weekDayList.contains(day.getWeek());
                }
            }
            return rangeFlag && workDayFlag;
        }).collect(Collectors.toList());
    }

}
