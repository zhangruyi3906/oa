package com.lh.oa.module.system.full.service.attendance.impl;

import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import com.lh.oa.module.system.controller.admin.wrapper.ProcessInstanceExtWrapper;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceStatisticInfoTo;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordStatisticsService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleService;
import com.lh.oa.module.system.utils.SysAttendanceRecordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author tanghanlin
 * @since 2023/11/11
 */
@Slf4j
@Service
public class SysAttendanceRecordStatisticsServiceImpl implements SysAttendanceRecordStatisticsService {

    /**
     * 当前各流程表单的中英文对应
     */
    private static final Map<String, String> PROCESS_MODEL_NAME_MAP = new HashMap();

    /**
     * 法定上午工作开始时间
     */
    private static final String MORNING_START_TIME_STR = " 09:00:00";

    /**
     * 法定上午工作结束时间
     */
    private static final String MORNING_END_TIME_STR = " 12:00:00";

    /**
     * 下午工作开始时间
     */
    private static final String AFTERNOON_START_TIME_STR = " 13:00:00";

    /**
     * 换算用的小时单位
     */
    private static final BigDecimal HOURS_UNIT = new BigDecimal(1000).multiply(new BigDecimal(60)).multiply(new BigDecimal(60));

    /**
     * 法定工作时间下的午休时长
     */
    private static final BigDecimal NOON_REST_HOURS = BigDecimal.ONE;

    /**
     * 法定工作时间下的上午工作时长
     */
    private static final int MORNING_WORK_HOURS = 3;

    /**
     * 法定工作时间下的下午工作时长
     */
    private static final int AFTERNOON_WORK_HOURS = 5;

    /**
     * 法定工作时间下的全天工作时长
     */
    private static final int ALL_DAY_WORK_HOURS = 8;

    static {
        // 填充需要计算的表单模型中文名称关系
        PROCESS_MODEL_NAME_MAP.put("leave", "qingjia");
        PROCESS_MODEL_NAME_MAP.put("add", "jiaban");
        PROCESS_MODEL_NAME_MAP.put("travel", "chuchai");
        PROCESS_MODEL_NAME_MAP.put("explain", "kaoqin");
    }

    @Resource
    private ProcessInstanceExtWrapper processInstanceExtWrapper;

    @Resource
    private SysAttendanceRuleService sysAttendanceRuleService;

    @Resource
    private SysAttendanceRecordService sysAttendanceRecordService;

    @Override
    public AttendanceStatisticInfoTo getUserAndProjectInstanceInfoTo(Map<Long, Set<Integer>> userIdAndProjectIds, Integer attendMonth,
                                                                     String processKey, String returnUnit) {
        log.info("根据流程key获取实例的间隔时间, userIdAndProjectIds:{}, attendMonth:{}, processKey:{}, returnUnit:{}",
                userIdAndProjectIds, attendMonth, processKey, returnUnit);
        Set<Integer> projectIds = userIdAndProjectIds.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        List<SysAttendanceRuleEntity> ruleTOS = sysAttendanceRuleService.getAttendanceRuleByProjectIds(projectIds);
        if (CollectionUtils.isEmpty(ruleTOS)) {
            return new AttendanceStatisticInfoTo();
        }
        Map<Integer, SysAttendanceRuleEntity> ruleMap = ruleTOS.stream().collect(Collectors.toMap(SysAttendanceRuleEntity::getProjectId, Function.identity()));
        // 用户-项目-打卡时间的映射关系
        Map<Integer, Map<Integer, List<Long>>> userIdAndProjectIdAndDatestampMap = sysAttendanceRecordService.getRecentlyMonthAttendanceDateList(
                new SysAttendanceRecordQueryParam(userIdAndProjectIds.keySet(), attendMonth));

        String modelName = PROCESS_MODEL_NAME_MAP.get(processKey);
        if (StringUtils.isBlank(modelName)) {
            return new AttendanceStatisticInfoTo();
        }
        // 获取所有请假的流程实例
        List<BpmProcessInstanceTo> processInstanceTos = processInstanceExtWrapper.getProcessByUserIdAndMonth(userIdAndProjectIds.keySet(), attendMonth, modelName);
        Map<Long, List<BpmProcessInstanceTo>> processInstanceGroupByUserId = processInstanceTos
                .stream()
                .collect(Collectors.groupingBy(BpmProcessInstanceTo::getStartUserId));

        Map<Integer, Map<Integer, BigDecimal>> hoursMap = new HashMap<>(16);
        Map<Integer, Map<Integer, List<Map<String, Object>>>> optionFromListMap = new HashMap<>(16);
        Map<Integer, Map<Integer, List<BpmProcessInstanceTo>>> processListMap = new HashMap<>(16);
        processInstanceGroupByUserId.forEach((userId, processList) -> {
            // 获取用户对应的几个项目内，发起流程的时间对应在哪个项目内，以发起流程开始时间为准，暂不考虑跨项目流程这种极端情况
            Map<Integer, List<Long>> projectIdAndMaxRecordDateMap = userIdAndProjectIdAndDatestampMap.getOrDefault(userId.intValue(), Collections.emptyMap());
            Map<Integer, BigDecimal> projectAndHoursMap = new HashMap<>(16);
            Map<Integer, List<Map<String, Object>>> projectAndOptionFromListMap = new HashMap<>(16);
            Map<Integer, List<BpmProcessInstanceTo>> projectAndProcessListMap = new HashMap<>(16);
            processList.forEach(process -> {
                // 解出流程业务表单内的开始时间和结束时间
                Date formStartTime = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(process.getFormStartTime()));
                Date formEndTime = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(process.getFormEndTime()));
                if (Objects.isNull(formStartTime) && Objects.isNull(formEndTime)) {
                    return;
                }

                // 获取发起流程前打卡的项目
                int finalProjectId;
                // 如果打卡记录是空的，比如入职后就一直没打卡，就关联到该用户下id最大的项目上，这属于是兜底处理了，避免新员工不打卡就看不见流程数据的情况
                if (projectIdAndMaxRecordDateMap.isEmpty()) {
                    Set<Integer> userProjectIds = userIdAndProjectIds.getOrDefault(userId, Collections.emptySet());
                    finalProjectId = userProjectIds.stream().max(Integer::compareTo).orElse(0);
                }
                // 如果只在一个项目内打了卡，则直接计算
                else if (projectIdAndMaxRecordDateMap.size() == 1) {
                    finalProjectId = new ArrayList<>(projectIdAndMaxRecordDateMap.keySet()).get(0);
                }
                // 如果在多个项目内打了卡，查这些时间之前的最近一次打卡记录是在哪个项目上，优先查之前的，在哪个项目上就算哪个项目的
                else {
                    finalProjectId = SysAttendanceRecordUtil.getFinalProjectIdByAttendanceRecordMap(projectIdAndMaxRecordDateMap, formEndTime);
                }
                if (Objects.nonNull(formStartTime) && Objects.nonNull(formEndTime)) {
                    SysAttendanceRuleEntity rule = ruleMap.get(finalProjectId);
                    if (Objects.isNull(rule)) {
                        return;
                    }
                    BigDecimal gapTimes = this.getGapTimeByStartTimeAndEndTime(process, processKey, rule, formStartTime, formEndTime, returnUnit);
                    BigDecimal allGapTimes = projectAndHoursMap.get(finalProjectId);
                    if (Objects.isNull(allGapTimes)) {
                        allGapTimes = BigDecimal.ZERO;
                    }
                    allGapTimes = allGapTimes.add(gapTimes);
                    projectAndHoursMap.put(finalProjectId, allGapTimes);
                }

                if (MapUtils.isNotEmpty(process.getFormVariables())) {
                    List<Map<String, Object>> formList = projectAndOptionFromListMap.get(finalProjectId);
                    if (CollectionUtils.isEmpty(formList)) {
                        formList = new LinkedList<>();
                        projectAndOptionFromListMap.put(finalProjectId, formList);
                    }
                    formList.add(process.getFormVariables());
                }

                List<BpmProcessInstanceTo> processInstanceList = projectAndProcessListMap.get(finalProjectId);
                if (CollectionUtils.isEmpty(processInstanceList)) {
                    processInstanceList = new LinkedList<>();
                    projectAndProcessListMap.put(finalProjectId, processInstanceList);
                }
                processInstanceList.add(process);
            });
            hoursMap.put(userId.intValue(), projectAndHoursMap);
            optionFromListMap.put(userId.intValue(), projectAndOptionFromListMap);
            processListMap.put(userId.intValue(), projectAndProcessListMap);
        });
        AttendanceStatisticInfoTo infoTo = new AttendanceStatisticInfoTo();
        infoTo.setUserAndProjectWorkTime(hoursMap);
        infoTo.setUserAndProjectProcessFormMap(optionFromListMap);
        infoTo.setUserAndProjectProcessMap(processListMap);
        return infoTo;
    }

    // 适配自由考勤和固定考勤的考勤统计规则
    private BigDecimal getGapTimeByStartTimeAndEndTime(BpmProcessInstanceTo processInstance, String processKey,
                                                       SysAttendanceRuleEntity rule, Date startTime, Date endTime, String returnUnit) {
        long startTimestamp = startTime.getTime();
        long endTimestamp = endTime.getTime();
        String startDayStr = TimeUtils.formatAsDate(startTime);
        String endDayStr = TimeUtils.formatAsDate(endTime);
        // 特殊情况1：开始时间和结束时间一致，直接结束，返回0
        if (Objects.equals(startTimestamp, endTimestamp)) {
            return BigDecimal.ZERO;
        }

        Boolean isFreedomRule = rule.isFreedomRule();
        // 获取开始日期和结束日期中午的间隔时间
        String morningEndTimeStr = isFreedomRule || Objects.isNull(rule.getNoonRestStartTime())
                ? MORNING_END_TIME_STR : " " + rule.getNoonRestStartTime();
        String afternoonStartTimeStr = isFreedomRule || Objects.isNull(rule.getNoonRestEndTime())
                ? AFTERNOON_START_TIME_STR : " " + rule.getNoonRestEndTime();
        Date startDayMorningEndTime = TimeUtils.parseAsDateTime(startDayStr + morningEndTimeStr);
        Date startDayAfternoonStartTime = TimeUtils.parseAsDateTime(startDayStr + afternoonStartTimeStr);
        Date endDayMorningEndTime = TimeUtils.parseAsDateTime(endDayStr + morningEndTimeStr);
        Date endDayAfternoonStartTime = TimeUtils.parseAsDateTime(endDayStr + afternoonStartTimeStr);
        if (Objects.isNull(startDayMorningEndTime) || Objects.isNull(startDayAfternoonStartTime)
                || Objects.isNull(endDayMorningEndTime) || Objects.isNull(endDayAfternoonStartTime)) {
            return BigDecimal.ZERO;
        }
        // 获取每日午休时间
        BigDecimal dailyNoonRestHours = this.getDailyNoonRestHours(isFreedomRule, startDayMorningEndTime, startDayAfternoonStartTime);
        // 获取每日工作时长
        BigDecimal dailyWorkHours = this.getDailyWorkHours(startDayStr, startDayMorningEndTime, startDayAfternoonStartTime, rule,
                dailyNoonRestHours, processInstance.getId(), processKey, processInstance.getStartUserId());

        // 获取开始和结束时间中间间隔的绝对天数
        int betweenDays = this.getBusinessBetweenDay(startTime, endTime, isFreedomRule, endDayStr, rule.getClockInTime(),
                processInstance.getId(), processKey, processInstance.getStartUserId());

        BigDecimal finalAllWorkTimes;
        // 开始时间和结束时间在一天内，这样直接计算开始时间和结束时间中间的间隔时间即可
        if (Objects.equals(0, betweenDays)) {
            finalAllWorkTimes = this.getWorkHoursOnSameDay(startTimestamp, endTimestamp, startDayMorningEndTime, endDayAfternoonStartTime,
                    dailyNoonRestHours, processInstance.getId(), processKey, processInstance.getStartUserId());
        } else {
            // 自由考勤没有上下班时间，就按早九晚六的八小时工作制算
            if (isFreedomRule) {
                finalAllWorkTimes = this.getWorkHoursNotOnSameDayWithFreedomRule(startTime, endTime, startDayMorningEndTime, endDayAfternoonStartTime,
                        betweenDays, processInstance.getId(), processKey, processInstance.getStartUserId());
            }
            // 固定考勤有上下班时间，所以需要单独细算
            else {
                finalAllWorkTimes = this.getWorkHoursNotOnSameDayWithFixedRule(startDayStr, endDayStr, startTimestamp, endTimestamp,
                        startDayMorningEndTime, startDayAfternoonStartTime, endDayMorningEndTime, endDayAfternoonStartTime,
                        rule, betweenDays, dailyNoonRestHours, processInstance.getId(), processKey, processInstance.getStartUserId());
            }
        }
        // 根据请求参数里的返回单位，换算后返回
        if (Objects.equals("DAY", returnUnit)) {
            finalAllWorkTimes = finalAllWorkTimes.divide(dailyWorkHours, 1, RoundingMode.HALF_UP);
            // 避免因为精度问题被抹零
            if (finalAllWorkTimes.compareTo(BigDecimal.ZERO) == 0) {
                finalAllWorkTimes = new BigDecimal("0.1");
            }
        }
        return finalAllWorkTimes;
    }

    private BigDecimal getDailyNoonRestHours(Boolean isFreedomRule, Date startDayMorningEndTime, Date startDayAfternoonStartTime) {
        // 自由考勤固定午休一小时，固定考勤计算得出午休时间
        if (isFreedomRule) {
            return NOON_REST_HOURS;
        } else {
            return new BigDecimal(startDayAfternoonStartTime.getTime() - startDayMorningEndTime.getTime())
                    .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
        }
    }

    private int getBusinessBetweenDay(Date startTime, Date endTime, Boolean isFreedomRule, String endDayStr, String clockInTime,
                                      Long processInstanceId, String processKey, Long startUserId) {
        Date endDateTime;
        if (isFreedomRule) {
            endDateTime = TimeUtils.parseAsDateTime(endDayStr + MORNING_START_TIME_STR);
        } else {
            endDateTime = TimeUtils.parseAsDateTime(endDayStr + " " + clockInTime);
        }
        if (Objects.isNull(endDateTime)) {
            return 0;
        }
        long endDayStartWorkTimestamp = endDateTime.getTime();
        // 获取开始和结束时间中间间隔的绝对天数
        int betweenDays = (int) TimeUtils.daysBetween(startTime, endTime);
        // 特殊情况2：开始结束日期相邻，且结束时间选到了第二天的上班时间之前，这种直接按开始结束时间在同一天内计算
        if (Objects.equals(1, betweenDays) && endTime.getTime() <= endDayStartWorkTimestamp) {
            betweenDays = 0;
        }
        return betweenDays;
    }

    private BigDecimal getWorkHoursOnSameDay(long startTimestamp, long endTimestamp,
                                             Date startDayMorningEndTime, Date endDayAfternoonStartTime,
                                             BigDecimal dailyNoonRestHours, Long processInstanceId,
                                             String processKey, Long startUserId) {
        long oneDayStamp = endTimestamp - startTimestamp;
        BigDecimal hours = new BigDecimal(oneDayStamp).divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
        // 避免因为精度问题被抹零
        if (hours.compareTo(BigDecimal.ZERO) == 0) {
            hours = new BigDecimal("0.1");
        }
        long startDayMorningEndTimestamp = startDayMorningEndTime.getTime();
        long endDayAfternoonStartTimestamp = endDayAfternoonStartTime.getTime();
        // 特殊情况3：如果开始时间和结束时间中包含了中午，需要减去午休时间
        if (startTimestamp <= startDayMorningEndTimestamp && endTimestamp >= endDayAfternoonStartTimestamp) {
            hours = hours.subtract(dailyNoonRestHours);
        }
        return hours;
    }

    private BigDecimal getWorkHoursNotOnSameDayWithFreedomRule(Date startTime, Date endTime,
                                                               Date startDayMorningEndTime, Date endDayAfternoonStartTime,
                                                               int betweenDays, Long processInstanceId, String processKey, Long startUserId) {
        long startDayMorningEndTimestamp = startDayMorningEndTime.getTime();
        long endDayAfternoonStartTimestamp = endDayAfternoonStartTime.getTime();
        int allWorkHours;

        // 开始时间和结束时间不在一天内，则各自计算开始和结束的工作日，除去开始和结束时间中间的天数 * 8即可
        // 因为拿不到可确定的考勤规则，这里只能先按早九晚六的八小时工作制算，上午按3小时,下午按5小时
        // 特殊情况:4：如果开始时间和结束时间一致，则开始时间和结束时间两天加起来按一整天算
        if (Objects.equals(TimeUtils.formatAsTime(startTime), TimeUtils.formatAsTime(endTime))) {
            allWorkHours = ALL_DAY_WORK_HOURS;
        } else {
            // 请假开始时间超过上午结束时间，就按一个下午计算，没超过则按一天计算
            int startWorkHours = startTime.getTime() > startDayMorningEndTimestamp ? AFTERNOON_WORK_HOURS : ALL_DAY_WORK_HOURS;
            // 请假结束时间超过下午开始时间，就按一天计算，没超过则按一个上午计算
            int endWorkHours = endTime.getTime() >= endDayAfternoonStartTimestamp ? ALL_DAY_WORK_HOURS : MORNING_WORK_HOURS;
            allWorkHours = startWorkHours + endWorkHours;
        }

        // 如果中间间隔天数超过一天,则工作小时数需要加上天数 * 8
        if (betweenDays > 1) {
            allWorkHours += (betweenDays - 1) * ALL_DAY_WORK_HOURS;
        }
        return new BigDecimal(allWorkHours);
    }

    private BigDecimal getWorkHoursNotOnSameDayWithFixedRule(String startDayStr, String endDayStr, long startTimestamp, long endTimestamp,
                                                             Date startDayMorningEndTime, Date startDayAfternoonStartTime, Date endDayMorningEndTime,
                                                             Date endDayAfternoonStartTime, SysAttendanceRuleEntity rule, int betweenDays,
                                                             BigDecimal dailyNoonRestHours, Long processInstanceId, String processKey, Long startUserId) {
        // 需要计算出开始和结束两天的工作时间，乘以 中间天数 - 1 * 每日工作时间 就是全部工作时间，同时要计算出上午和下午的工作时间
        // 获取考勤规则里的开始天和结束天工作时间的上下限
        Date ruleStartDayStartTimes = TimeUtils.parseAsDateTime(startDayStr + " " + rule.getClockInTime());
        Date ruleStartDayEndTimes = TimeUtils.parseAsDateTime(startDayStr + " " + rule.getClockOffTime());
        Date ruleEndDayStartTimes = TimeUtils.parseAsDateTime(endDayStr + " " + rule.getClockInTime());
        Date ruleEndDayEndTimes = TimeUtils.parseAsDateTime(endDayStr + " " + rule.getClockOffTime());
        long startDayMorningEndTimestamp = startDayMorningEndTime.getTime();
        long startDayAfternoonStartTimestamp = startDayAfternoonStartTime.getTime();
        long endDayMorningEndTimestamp = endDayMorningEndTime.getTime();
        long endDayAfternoonStartTimestamp = endDayAfternoonStartTime.getTime();

        if (Objects.isNull(ruleStartDayStartTimes) || Objects.isNull(ruleStartDayEndTimes)
                || Objects.isNull(ruleEndDayStartTimes) || Objects.isNull(ruleEndDayEndTimes)) {
            return BigDecimal.ZERO;
        }
        long startDayStartTimestamp = ruleStartDayStartTimes.getTime();
        long startDayEndTimestamp = ruleStartDayEndTimes.getTime();
        long endDayStartTimestamp = ruleEndDayStartTimes.getTime();
        long endDayEndTimestamp = ruleEndDayEndTimes.getTime();

        // 获取上下午的标准工作时间
        BigDecimal dayMorningWorkHours = BigDecimal.ZERO;
        if (startDayStartTimestamp < startDayMorningEndTimestamp) {
            dayMorningWorkHours = new BigDecimal(startDayMorningEndTimestamp - startDayStartTimestamp)
                    .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
        }
        BigDecimal dayAfternoonWorkHours = BigDecimal.ZERO;
        if (endDayAfternoonStartTimestamp < endDayEndTimestamp) {
            dayAfternoonWorkHours = new BigDecimal(endDayEndTimestamp - endDayAfternoonStartTimestamp)
                    .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
        }

        BigDecimal startDayWorkHours = BigDecimal.ZERO;
        // 特殊情况5：开始时间在中午，则开始时间按下午开始时间算
        if (startDayMorningEndTimestamp <= startTimestamp && startTimestamp <= startDayAfternoonStartTimestamp) {
            startDayWorkHours = dayAfternoonWorkHours;
        }
        if (startDayEndTimestamp > startTimestamp) {
            // 工作时长 = 工作结束时间 - 开始时间 - 午休时间
            startDayWorkHours = new BigDecimal(startDayEndTimestamp - startTimestamp)
                    .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
            // 如果开始时间在上午，就需要减去午休的一小时
            if (startDayMorningEndTimestamp >= startTimestamp) {
                startDayWorkHours = startDayWorkHours.subtract(dailyNoonRestHours);
            }
        }

        BigDecimal endDayWorkHours = BigDecimal.ZERO;
        // 特殊情况6：结束时间在中午，则结束时间按上午结束时间算
        if (endDayMorningEndTimestamp <= endTimestamp && endTimestamp <= endDayAfternoonStartTimestamp) {
            endDayWorkHours = dayMorningWorkHours;
        }
        if (endTimestamp > endDayStartTimestamp) {
            // 工作时长 = 工作结束时间 - 开始时间 - 午休时间
            endDayWorkHours = new BigDecimal(endTimestamp - endDayStartTimestamp)
                    .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
            // 如果结束时间在下午，就需要减去午休的一小时
            if (endDayAfternoonStartTimestamp <= endTimestamp) {
                endDayWorkHours = endDayWorkHours.subtract(dailyNoonRestHours);
            }
        }

        BigDecimal allWorkHours = startDayWorkHours.add(endDayWorkHours);
        // 避免因为精度问题被抹零
        if (allWorkHours.compareTo(BigDecimal.ZERO) == 0) {
            allWorkHours = new BigDecimal("0.1");
        }
        // 如果中间间隔天数超过一天,则工作小时数需要加上天数 * 8
        if (betweenDays > 1) {
            BigDecimal days = new BigDecimal(betweenDays).subtract(BigDecimal.ONE);
            BigDecimal dailyWorkHours = dayMorningWorkHours.add(dayAfternoonWorkHours);
            allWorkHours = allWorkHours.add(days.multiply(dailyWorkHours));
        }
        return allWorkHours;
    }

    private BigDecimal getDailyWorkHours(String startDayStr, Date startDayMorningEndTime, Date startDayAfternoonStartTime,
                                         SysAttendanceRuleEntity rule, BigDecimal dailyNoonRestHour,
                                         Long processInstanceId, String processKey, Long startUserId) {
        if (rule.isFreedomRule()) {
            return new BigDecimal(ALL_DAY_WORK_HOURS);
        }
        Date ruleStartDayStartTimes = TimeUtils.parseAsDateTime(startDayStr + " " + rule.getClockInTime());
        Date ruleStartDayEndTimes = TimeUtils.parseAsDateTime(startDayStr + " " + rule.getClockOffTime());
        if (Objects.isNull(ruleStartDayStartTimes) || Objects.isNull(ruleStartDayEndTimes)) {
            return BigDecimal.ZERO;
        }

        long startDayMorningEndTimestamp = startDayMorningEndTime.getTime();
        long startDayAfternoonStartTimestamp = startDayAfternoonStartTime.getTime();

        long startDayStartTimestamp = ruleStartDayStartTimes.getTime();
        long startDayEndTimestamp = ruleStartDayEndTimes.getTime();

        BigDecimal dailyWorkHours = new BigDecimal(startDayEndTimestamp - startDayStartTimestamp)
                .divide(HOURS_UNIT, 1, RoundingMode.HALF_UP);
        // 如果开始时间和结束时间之间包括了午休时间，那么工作时间减去一小时
        if ((startDayStartTimestamp <= startDayMorningEndTimestamp && startDayAfternoonStartTimestamp <= startDayEndTimestamp)) {
            dailyWorkHours = dailyWorkHours.subtract(dailyNoonRestHour);
        }
        return dailyWorkHours;
    }

}
