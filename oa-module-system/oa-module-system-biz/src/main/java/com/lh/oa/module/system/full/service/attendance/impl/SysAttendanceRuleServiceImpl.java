package com.lh.oa.module.system.full.service.attendance.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.system.controller.admin.userProject.param.UserProjectRuleSameRelationParam;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRulePositionEntity;
import com.lh.oa.module.system.full.entity.attandance.vo.SysAttendanceRuleHolidayVo;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import com.lh.oa.module.system.full.enums.attendance.LegalHolidayStateEnum;
import com.lh.oa.module.system.full.enums.attendance.OffsiteClockStateEnum;
import com.lh.oa.module.system.full.mapper.attendance.SysAttendanceRuleMapper;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleHolidayService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRulePositionService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleService;
import com.lh.oa.module.system.full.service.attendance.UserProjectRuleSameRelationService;
import com.lh.oa.module.system.full.utils.DistanceUtil;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.holidayInfo.HolidayInfoService;
import com.lh.oa.module.system.service.user.AdminUserService;
import com.lh.oa.module.system.service.userProject.UserProjectService;
import com.lh.oa.module.system.service.wrapper.JntTokenWrapper;
import com.lh.oa.module.system.util.attendance.SysAttendanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.module.system.full.enums.FlagStateEnum.ENABLED;
import static com.lh.oa.module.system.full.utils.Utils.unixTime;

@Slf4j
@Service
public class SysAttendanceRuleServiceImpl implements SysAttendanceRuleService {

    /**
     * 考勤提醒时间间隔阈值，目前设置为30分钟
     */
    private final long REMIND_INTERVAL_TIMESTAMP = 1800000L;

    @Value("${jnt.platform-url}")
    private String platformUrl;

    @Value("${jnt.record-remind-url}")
    private String recordRemindUrl;

    @Autowired
    private SysAttendanceRuleMapper mapper;
    @Autowired
    private SysAttendanceRulePositionService sysAttendanceRulePositionService;
    @Autowired
    private SysAttendanceRecordService sysAttendanceRecordService;
    @Autowired
    private AdminUserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private UserProjectService userProjectService;

    @Resource
    private SysAttendanceRuleHolidayService sysAttendanceRuleHolidayService;

    @Resource
    private UserProjectRuleSameRelationService userProjectRuleSameRelationService;

    @Resource
    private JntTokenWrapper jntTokenWrapper;

    @Resource
    private HolidayInfoService holidayInfoService;

    @Resource
    private SysAttendanceRuleService sysAttendanceRuleService;

    @Transactional
    @Override
    public void saveAttendanceRule(SysAttendanceRuleEntity attendanceRule, int userId) {
        if ((attendanceRule.getDeptId() == null || attendanceRule.getDeptId() == 0)
                && (attendanceRule.getProjectId() == null || attendanceRule.getProjectId() == 0))
            throw new BusinessException("考勤规则所属部门/项目不能为空，请确认后再提交！");
        if (StrUtil.isBlank(attendanceRule.getName()))
            throw new BusinessException("考勤规则名称不能为空，请确认后再提交！");
        if (attendanceRule.getAttendanceType() == null)
            throw new BusinessException("考勤规则类型不能为空，请确认后再提交！");
        if (StrUtil.isBlank(attendanceRule.getWeekday()))
            throw new BusinessException("工作日不能为空，请确认后再提交！");
        if (ObjectUtil.isEmpty(attendanceRule.getAttendRulePositionList()))
            throw new BusinessException("打卡地点不能为空，请确认后再提交！");
        if (attendanceRule.getAttendanceType() == AttendanceTypeEnum.FIXED_ATTENDANCE) {
            if (StrUtil.isBlank(attendanceRule.getClockInTime())) {
                throw new BusinessException("上班时间不能为空，请确认后再提交！");
            }
            if (StrUtil.isBlank(attendanceRule.getClockOffTime())) {
                throw new BusinessException("下班时间不能为空，请确认后再提交！");
            }
            if (!DatePattern.NORM_TIME_PATTERN.matches(attendanceRule.getClockInTime())) {
                attendanceRule.setClockInTime(DateUtil.parse(attendanceRule.getClockInTime(), "HH:mm").toTimeStr());
            }
            if (!DatePattern.NORM_TIME_PATTERN.matches(attendanceRule.getClockOffTime())) {
                attendanceRule.setClockOffTime(DateUtil.parse(attendanceRule.getClockOffTime(), "HH:mm").toTimeStr());
            }
            if (Objects.nonNull(attendanceRule.getNoonRestStartTime())
                    && !DatePattern.NORM_TIME_PATTERN.matches(attendanceRule.getNoonRestStartTime())) {
                attendanceRule.setNoonRestStartTime(DateUtil.parse(attendanceRule.getNoonRestStartTime(), "HH:mm").toTimeStr());
            }
            if (Objects.nonNull(attendanceRule.getNoonRestEndTime())
                    && !DatePattern.NORM_TIME_PATTERN.matches(attendanceRule.getNoonRestEndTime())) {
                attendanceRule.setNoonRestEndTime(DateUtil.parse(attendanceRule.getNoonRestEndTime(), "HH:mm").toTimeStr());
            }
        }
        if (attendanceRule.getLegalHolidayState() == null) {
            attendanceRule.setLegalHolidayState(LegalHolidayStateEnum.NOT_SYNC_HOLIDAY);
        }
        if (attendanceRule.getOffsiteClockState() == null) {
            attendanceRule.setOffsiteClockState(OffsiteClockStateEnum.FORBID_OFFSITE);
        }
        if (Objects.nonNull(attendanceRule.getDeptId()) && Objects.equals(attendanceRule.getId(), 0)) {
            SysAttendanceRuleEntity ruleEntity = mapper.getAttendanceRuleByDeptId(attendanceRule.getDeptId());
            ExceptionThrowUtils.throwIfTrue(Objects.nonNull(ruleEntity), "该部门已有考勤规则，请勿重复配置");
        }
        if (Objects.nonNull(attendanceRule.getProjectId()) && Objects.equals(attendanceRule.getId(), 0)) {
            SysAttendanceRuleEntity ruleEntity = mapper.getAttendanceRuleByProjectId(attendanceRule.getProjectId());
            ExceptionThrowUtils.throwIfTrue(Objects.nonNull(ruleEntity), "该项目已有考勤规则，请勿重复配置");
        }
        int attendRuleId = attendanceRule.getId();
        if (attendRuleId == 0) {
            attendanceRule.setCreatedBy(userId);
            attendanceRule.setCreatedTime(unixTime());
        } else {
            attendanceRule.setModifiedBy(userId);
            attendanceRule.setModifiedTime(unixTime());
        }
        attendanceRule.setFlag(ENABLED.value());
        SysAttendanceRuleEntity attendanceRuleListByName = mapper.getAttendanceRuleListByName(attendanceRule.getName());
        if (attendanceRuleListByName == null || attendanceRuleListByName.getId() == attendRuleId) {
            List<SysAttendanceRuleEntity> attendanceRuleListByOthers = mapper.getAttendanceRuleListByOthers(attendanceRule.getDeptId(), attendanceRule.getProjectId(), attendanceRule.getAttendanceType());
            if ((attendRuleId == 0 && attendanceRuleListByOthers.size() == 0)
                    || (attendRuleId != 0 && attendanceRuleListByOthers.size() == 1 && attendanceRuleListByOthers.get(0).getId() == attendRuleId)) {
                mapper.saveAttendanceRule(attendanceRule);
                List<SysAttendanceRulePositionEntity> attendRulePositionList = attendanceRule.getAttendRulePositionList();
                if (attendRuleId > 0 || CollUtil.isEmpty(attendRulePositionList)) {
                    sysAttendanceRulePositionService.deleteAttendanceRulePositionByAttendanceRuleId(attendanceRule.getId(), userId);
                }
                if (CollUtil.isNotEmpty(attendRulePositionList)) {
                    for (int i = 0; i < attendRulePositionList.size(); i++) {
                        SysAttendanceRulePositionEntity attendanceRulePosition = attendRulePositionList.get(i);
                        attendanceRulePosition.setDeptId(attendanceRule.getDeptId());
                        attendanceRulePosition.setProjectId(attendanceRule.getProjectId());
                        attendanceRulePosition.setAttendanceRuleId(attendanceRule.getId());
                        attendanceRulePosition.setOrderNumber(i);
                        sysAttendanceRulePositionService.saveAttendanceRulePosition(attendanceRulePosition, userId);
                    }
                }
                sysAttendanceRuleHolidayService.save(attendanceRule, attendanceRule.getCustomHolidayList());
                // 重置项目考勤规则相似关系
                userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(null, attendanceRule.getProjectId()));
            } else {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_RULE_DEPT_PROJECT_IS_EXISTS);
            }
        } else {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_RULE_NAME_IS_EXISTS);
        }
    }

    @Override
    public SysAttendanceRuleEntity getAttendanceRuleById(int id) {
        if (id == 0)
            return null;
        SysAttendanceRuleEntity attendanceRule = mapper.getAttendanceRuleById(id);
        if (Objects.isNull(attendanceRule)) {
            return null;
        }
        attendanceRule.setAttendRulePositionList(sysAttendanceRulePositionService.getAttendanceRulePositionListByAttendanceRuleId(id));
        List<SysAttendanceRuleHoliday> holidays = sysAttendanceRuleHolidayService.queryListByRuleIds(Collections.singleton((long) attendanceRule.getId()));
        this.buildRuleHolidayInfo(attendanceRule, holidays);
        return attendanceRule;
    }

    @Override
    public List<SysAttendanceRuleEntity> getAttendanceRuleList(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType, Pageable pageable) {
        List<SysAttendanceRuleEntity> result = mapper.getAttendanceRuleList(StrUtil.isBlank(name) ? null : "%" + name + "%", StrUtil.isBlank(deptName) ? null : "%" + deptName + "%",
                        StrUtil.isBlank(projectName) ? null : "%" + projectName + "%", attendanceType, pageable)
                .parallelStream().peek(attendanceRule ->
                        attendanceRule.setAttendRulePositionList(sysAttendanceRulePositionService.getAttendanceRulePositionListByAttendanceRuleId(attendanceRule.getId()))
                ).collect(Collectors.toList());
        if (result.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysAttendanceRuleHoliday> holidayVos = sysAttendanceRuleHolidayService.queryListByRuleIds(result
                .stream()
                .map(rule -> (long) rule.getId())
                .collect(Collectors.toSet()));
        Map<Long, List<SysAttendanceRuleHoliday>> holidayMapByRuleId = holidayVos
                .stream()
                .collect(Collectors.groupingBy(SysAttendanceRuleHoliday::getSysAttendanceRuleId));
        result.forEach(rule -> {
            List<SysAttendanceRuleHoliday> holidays = holidayMapByRuleId.get((long) rule.getId());
            this.buildRuleHolidayInfo(rule, holidays);
        });
        return result;
    }

    private void buildRuleHolidayInfo(SysAttendanceRuleEntity rule, List<SysAttendanceRuleHoliday> holidays) {
        rule.setEnableCustomHoliday(false);
        if (!CollectionUtils.isEmpty(holidays)) {
            List<SysAttendanceRuleHolidayVo> holidayVoList = holidays.stream().map(holiday -> {
                SysAttendanceRuleHolidayVo holidayVo = JsonUtils.covertObject(holiday, SysAttendanceRuleHolidayVo.class);
                holidayVo.setHolidayDate(TimeUtils.formatAsDate(holiday.getHolidayDate()));
                return holidayVo;
            }).collect(Collectors.toList());
            rule.setCustomHolidayList(holidayVoList);
            rule.setEnableCustomHoliday(true);
        }
    }

    @Override
    public int getAttendanceRuleCount(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType) {
        return mapper.getAttendanceRuleCount(StrUtil.isBlank(name) ? null : "%" + name + "%", StrUtil.isBlank(deptName) ? null : "%" + deptName + "%",
                StrUtil.isBlank(projectName) ? null : "%" + projectName + "%", attendanceType);
    }

    @Transactional
    @Override
    public void deleteAttendanceRule(String ids, int userId) {
        if (StrUtil.isBlank(ids))
            throw new BusinessException("所选考勤规则为空，请确认后再提交！");
        int[] arr = StrUtil.splitToInt(ids, ",");
        for (int id : arr) {
            SysAttendanceRuleEntity rule = mapper.getAttendanceRuleById(id);
            ExceptionThrowUtils.throwIfNull(rule, "考勤规则不存在");
            mapper.deleteAttendanceRule(id, unixTime(), userId);
            sysAttendanceRulePositionService.deleteAttendanceRulePositionByAttendanceRuleId(id, userId);
            // 重置项目考勤规则相似关系
            userProjectRuleSameRelationService.resetRuleSameRelation(new UserProjectRuleSameRelationParam(null, rule.getProjectId()));
        }
    }

    @Override
    public SysAttendanceRuleEntity getAttendanceDistanceRule(int deptId, int projectId, int userId) {
        SysAttendanceRuleEntity attendanceRule = null;
        if (projectId > 0)
            attendanceRule = mapper.getAttendanceRuleByProjectId(projectId);
        if (attendanceRule == null && deptId > 0)
            attendanceRule = mapper.getAttendanceRuleByDeptId(deptId);
        if (attendanceRule == null) {
            AdminUserDO user = userService.getUser((long) userId);
            if (user != null) {
                attendanceRule = this.getDeptAttendanceRule(user.getDeptId());
            }
        }

        if (attendanceRule != null)
            attendanceRule.setAttendRulePositionList(sysAttendanceRulePositionService.getAttendanceRulePositionListByAttendanceRuleId(attendanceRule.getId()));
        return attendanceRule;
    }

    @Override
    public SysAttendanceRuleEntity getAttendanceDistanceRule(int deptId, List<Integer> projectIds, int userId, BigDecimal longitude, BigDecimal latitude) {
        SysAttendanceRuleEntity attendanceRule = null;
        if (ArrayUtil.isNotEmpty(projectIds)) {
            if (projectIds.size() > 1 && (longitude == null || latitude == null))
                throw new BusinessException("经纬度传入有误");
            StringJoiner projectIdsSj = new StringJoiner(",", "(", ")");
            for (Integer projectId : projectIds) {
                projectIdsSj.add(projectId + "");
            }
            List<SysAttendanceRuleEntity> attendanceRules = mapper.getAttendanceRuleByProjectIds(projectIdsSj.toString());
            // 获取项目考勤位置信息
            Map<Integer, List<SysAttendanceRulePositionEntity>> getRulePositionByRuleIds = new HashMap<>();
            Set<Integer> ruleIds = attendanceRules.stream().map(SysAttendanceRuleEntity::getId).collect(Collectors.toSet());
            if (!ruleIds.isEmpty()) {
                String ruleIdsStr = "(" + Joiner.on(",").join(ruleIds) + ")";
                getRulePositionByRuleIds = sysAttendanceRulePositionService.getRulePositionByRuleIds(ruleIdsStr);
            }

            if (attendanceRules.size() > 0) {
                if (projectIds.size() == 1) {
                    attendanceRule = attendanceRules.get(0);
                } else {
                    for (SysAttendanceRuleEntity rule : attendanceRules) {
                        List<SysAttendanceRulePositionEntity> positionList = getRulePositionByRuleIds.get(rule.getId());
                        if (!CollectionUtils.isEmpty(positionList)) {
                            if (positionList.size() > 1) {
                                // 如果有多个位置信息，挨个判断是否匹配当前经纬度
                                double preVal = 0;
                                for (SysAttendanceRulePositionEntity position : positionList) {
                                    double distance = DistanceUtil.distance(position.getLongitude().doubleValue(), position.getLatitude().doubleValue(),
                                            longitude.doubleValue(), latitude.doubleValue());
                                    if (distance == 0) {
                                        attendanceRule = rule;
                                        break;
                                    }
                                    if (preVal == 0 || preVal >= distance) {
                                        preVal = distance;
                                        attendanceRule = rule;
                                    }
                                }
                            } else {
                                // 如果只有一个位置信息，
                                attendanceRule = rule;
                            }
                        }
                    }
                }
            }
        } else if (deptId > 0) {
            attendanceRule = mapper.getAttendanceRuleByDeptId(deptId);
        } else {
            AdminUserDO user = userService.getUser((long) userId);
            if (user != null) {
                attendanceRule = this.getDeptAttendanceRule(user.getDeptId());
            }
        }
        return attendanceRule;
    }

    @Override
    public SysAttendanceRuleEntity getAttendanceRuleAndRecord(int deptId, int projectId, int userId, int attendanceDate, BigDecimal longitude, BigDecimal latitude) {
//        SysAttendanceRuleEntity attendanceRule = null;
//        if (deptId == 0 && projectId == 0) {
//            UserProjectExportReqVO reqVO = new UserProjectExportReqVO();
//            reqVO.setUserId((long) userId);
//            List<UserProjectDO> userProjectList = userProjectService.getUserProjectList(reqVO);
//            if (userProjectList != null && !userProjectList.isEmpty()) {
//                List<Integer> projectIds = userProjectList.stream().map(up -> Math.toIntExact(up.getProjectId())).collect(Collectors.toList());
//                attendanceRule = this.getAttendanceDistanceRule(deptId, projectIds, userId, longitude, latitude);
//            }
//        }
//        if (attendanceRule == null)
        SysAttendanceRuleEntity attendanceRule = this.getAttendanceDistanceRule(deptId, projectId, userId);
        if (attendanceRule == null) {
            log.error("所属项目、所属部门未配置考勤规则");
            throw new BusinessException("未配置考勤规则，请联系管理员");
        }
        if (attendanceDate == 0)
            attendanceDate = unixTime(DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN));
        attendanceRule.setAttendanceRecord(sysAttendanceRecordService.getAttendanceRecordByUserIdAndUnixDate(userId, attendanceDate, projectId));
        return attendanceRule;
    }

    @Override
    public List<SysAttendanceRuleEntity> getAttendanceRuleByProjectIds(Set<Integer> projectIds) {
        return mapper.getAttendanceRuleByProjectIds("(" + Joiner.on(",").join(projectIds) + ")");
    }

    @Override
    public void pushRegularAttendanceRemindMessage() {
        log.info("开始处理固定考勤打卡提醒推送消息");
        List<SysAttendanceRuleEntity> regularRuleList = mapper.selectListByType(AttendanceTypeEnum.FIXED_ATTENDANCE);
        if (regularRuleList.isEmpty()) {
            return;
        }
        List<HolidayInfoDO> dayList = Collections.singletonList(holidayInfoService.getByDate(new Date()));
        Set<Long> ruleIds = regularRuleList.stream().map(rule -> (long) rule.getId()).collect(Collectors.toSet());
        List<SysAttendanceRuleHoliday> holidayList = sysAttendanceRuleHolidayService.queryListByRuleIds(ruleIds);
        Map<Long, List<SysAttendanceRuleHoliday>> holidayMapByRuleId = holidayList
                .stream()
                .collect(Collectors.groupingBy(SysAttendanceRuleHoliday::getSysAttendanceRuleId));

        List<String> needRemindProjectMap = new LinkedList<>();
        String dateStr = TimeUtils.formatAsDate(new Date());
        regularRuleList.forEach(rule -> {
            Date clockInTime = TimeUtils.parseAsDateTime(dateStr + " " + rule.getClockInTime());
            if (Objects.isNull(clockInTime)) {
                return;
            }
            Date clockOffTime = TimeUtils.parseAsDateTime(dateStr + " " + rule.getClockOffTime());
            if (Objects.isNull(clockOffTime)) {
                return;
            }
            List<SysAttendanceRuleHoliday> holidays = holidayMapByRuleId.get((long) rule.getId());

            List<HolidayInfoDO> workDayList = SysAttendanceUtil.getWorkDayList(dayList, rule, holidays, null, null);
            if (workDayList.isEmpty()) {
                log.info("处理固定考勤打卡提醒推送消息-该项目今日不需要打卡，不进行推送，projectId:{}", rule.getProjectId());
                return;
            }

            // 过滤出间隔时间阈值内即将到达打卡时间的考勤规则，上下班时间都需要过滤
            long currentTimestamp = TimeUtils.currentMilli();
            long clockInTimestamp = clockInTime.getTime();
            long clockInIntervalTimestamp = clockInTimestamp - currentTimestamp;
            if (0L <= clockInIntervalTimestamp && clockInIntervalTimestamp <= REMIND_INTERVAL_TIMESTAMP) {
                needRemindProjectMap.add(rule.getProjectId() + "-" + "您距离上班打卡时间 " + rule.getClockInTime() + " 很近了，记得及时打卡哟");
            }
            long clockOffTimestamp = clockOffTime.getTime();
            long clockOffIntervalTimestamp = clockOffTimestamp - currentTimestamp;
            if (0L <= clockOffIntervalTimestamp && clockOffIntervalTimestamp <= REMIND_INTERVAL_TIMESTAMP) {
                needRemindProjectMap.add(rule.getProjectId() + "-" + "您距离下班打卡时间 " + rule.getClockOffTime() + " 很近了，记得及时打卡哟");
            }
        });
        if (!needRemindProjectMap.isEmpty()) {
            String jntAccessToken = jntTokenWrapper.getJntAccessToken();
            String jntRecordRemindUrl = platformUrl + recordRemindUrl;
            HttpRequest request = HttpRequest.post(jntRecordRemindUrl).body(JsonUtils.toJsonString(needRemindProjectMap));
            log.info("通知PMS发送考勤打卡提示信息, url:{}, params:{}", jntRecordRemindUrl, JsonUtils.toJsonString(needRemindProjectMap));
            HttpResponse httpResponse = request.bearerAuth(jntAccessToken).timeout(1000 * 60).execute();
            log.info("通知PMS发送考勤打卡提示信息-返回结果, url:{}, params:{}, response:{}, body:{}",
                    jntRecordRemindUrl, JsonUtils.toJsonString(needRemindProjectMap), httpResponse.getStatus(), httpResponse.body());
        }
    }

    /**
     * 向上查
     */
    private SysAttendanceRuleEntity getDeptAttendanceRule(Long deptId) {
        if (deptId == null || deptId == 0)
            return null;
        SysAttendanceRuleEntity attendanceRule = mapper.getAttendanceRuleByDeptId(Math.toIntExact(deptId));
        if (attendanceRule == null) {
            DeptDO dept = deptService.getDept(deptId);
            if (dept != null)
                return getDeptAttendanceRule(dept.getParentId());
        }
        return attendanceRule;
    }

}
