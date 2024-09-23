package com.lh.oa.module.system.full.service.attendance.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.excel.EasyExcel;
import com.google.common.base.Joiner;
import com.lh.oa.framework.common.exception.BusinessException;
import com.lh.oa.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.lh.oa.framework.common.exception.util.ExceptionThrowUtils;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.common.util.json.JsonUtils;
import com.lh.oa.framework.common.util.time.TimeTransUtils;
import com.lh.oa.framework.common.util.time.TimeUtils;
import com.lh.oa.module.bpm.api.task.dto.BpmProcessInstanceTo;
import com.lh.oa.module.bpm.enums.definition.AttendanceExplainProcessOptionEnum;
import com.lh.oa.module.system.api.sysAttendanceRule.to.SysAttendanceRecordQueryParam;
import com.lh.oa.module.system.controller.admin.information.vo.InformationExportReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.dal.dataobject.dept.DeptDO;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.project.ProjectDO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import com.lh.oa.module.system.full.constants.ISysConstant;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRecordEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleHoliday;
import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRulePositionEntity;
import com.lh.oa.module.system.full.entity.attandance.UserProjectRuleSameRelation;
import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceRecordDTO;
import com.lh.oa.module.system.full.entity.attandance.dto.AttendanceStatisticInfoTo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceRecordExcelVo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticExcelVo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticInfoVo;
import com.lh.oa.module.system.full.entity.attandance.vo.AttendanceStatisticProjectInfoVo;
import com.lh.oa.module.system.full.entity.base.InfoEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceClockStatusEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceStatusEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import com.lh.oa.module.system.full.enums.attendance.OffsiteClockStateEnum;
import com.lh.oa.module.system.full.mapper.attendance.SysAttendanceRecordMapper;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRecordStatisticsService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleHolidayService;
import com.lh.oa.module.system.full.service.attendance.SysAttendanceRuleService;
import com.lh.oa.module.system.full.service.attendance.UserProjectRuleSameRelationService;
import com.lh.oa.module.system.full.utils.DistanceUtil;
import com.lh.oa.module.system.full.utils.ReaderTxtFileUtil;
import com.lh.oa.module.system.service.dept.DeptService;
import com.lh.oa.module.system.service.holidayInfo.HolidayInfoService;
import com.lh.oa.module.system.service.information.InformationService;
import com.lh.oa.module.system.service.project.ProjectService;
import com.lh.oa.module.system.service.user.AdminUserService;
import com.lh.oa.module.system.util.attendance.SysAttendanceUtil;
import com.lh.oa.module.system.utils.SysAttendanceRecordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lh.oa.framework.security.core.util.SecurityFrameworkUtils.loginUserId;
import static com.lh.oa.module.system.full.enums.FlagStateEnum.ENABLED;
import static com.lh.oa.module.system.full.utils.Utils.toDateFormat;
import static com.lh.oa.module.system.full.utils.Utils.unixTime;

@Slf4j
@Service
public class SysAttendanceRecordServiceImpl implements SysAttendanceRecordService {

    /**
     * 考勤补充说明的选择项字段
     */
    private final String ATTENDANCE_EXPLAIN_OPTION_COLUMN_NAME = "select";

    @Autowired
    private SysAttendanceRecordMapper mapper;
    @Autowired
    private SysAttendanceRuleService sysAttendanceRuleService;
    @Autowired
    private AdminUserService userService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private ProjectService projectService;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private HolidayInfoService holidayInfoService;

    @Value("${oa.legal.holiday.url}")
    private String oaLegalHolidayUrl;

    @Resource
    private SysAttendanceRuleHolidayService sysAttendanceRuleHolidayService;

    @Resource
    private SysAttendanceRecordStatisticsService sysAttendanceRecordStatisticsService;

    @Resource
    private UserProjectRuleSameRelationService userProjectRuleSameRelationService;

    @Transactional
    @Override
    public String saveAttendanceRecord(AttendanceRecordDTO recordDTO, int userId) {
        log.info("发起考勤打卡，userId:{}, params:{}", userId, JsonUtils.toJsonString(recordDTO));
        String resultMessage = ISysConstant.CLOCK_IN_MESSAGE_OF_TODAY;
        if (recordDTO.getDeptId() == 0 && recordDTO.getProjectId() == 0)
            throw new BusinessException("未选择项目/部门，请确认后再提交");
        SysAttendanceRuleEntity attendanceRule = sysAttendanceRuleService.getAttendanceDistanceRule(recordDTO.getDeptId(), recordDTO.getProjectId(), userId);
        if (attendanceRule == null)
            throw new BusinessException("所属项目/部门未配置考勤规则，请联系管理员补充");
        if (recordDTO.getClockLongitude() == null || recordDTO.getClockLatitude() == null)
            throw new BusinessException("未获取到经纬度信息，请确认后提交");

        String attendanceDateStr = StrUtil.isBlank(recordDTO.getClockDate()) ? DateUtil.today() : recordDTO.getClockDate();
//        int[] workWeeks = StrUtil.splitToInt(attendanceRule.getWeekday(), ",");
//        if (!WeekEnum.contain(workWeeks, WeekEnum.of(attendanceDateStr)))
//            throw new BusinessException("今日不考勤，无需打卡");
//        if (attendanceRule.getAttendanceType() == AttendanceTypeEnum.FIXED_ATTENDANCE
//                && attendanceRule.getLegalHolidayState() == LegalHolidayStateEnum.SYNC_HOLIDAY && isLegalHoliday(attendanceDateStr))
//            throw new BusinessException("节假日不考勤，无需打卡");

        String clockTime = StrUtil.isBlank(recordDTO.getClockTime()) ? DateUtil.date().toTimeStr() : recordDTO.getClockTime();
        int attendanceDate = unixTime(DateUtil.parse(attendanceDateStr, DatePattern.NORM_DATE_PATTERN));
        // 如果此次自由打卡记为第二天，签到时间仍维持真实时间
        String clockDateTime = attendanceDateStr + " " + clockTime;
        if (attendanceRule.getAttendanceType() == AttendanceTypeEnum.FREEDOM_ATTENDANCE
                && StrUtil.isNotBlank(attendanceRule.getCutOffTime()) && attendanceRule.getCutOffTime().compareTo(clockTime) < 0) {
            DateTime dateTime = DateUtil.parse(DateUtil.offsetDay(DateUtil.parse(attendanceDateStr, DatePattern.NORM_DATE_PATTERN), 1).toDateStr(), DatePattern.NORM_DATE_PATTERN);
            attendanceDateStr = dateTime.toDateStr();
            attendanceDate = unixTime(dateTime);
            resultMessage = ISysConstant.CLOCK_IN_MESSAGE_OF_TOMORROW;
        }
        List<SysAttendanceRulePositionEntity> attendanceRulePositionList = attendanceRule.getAttendRulePositionList();
        if (attendanceRulePositionList == null)
            throw new BusinessException("所属项目/部门未配置考勤地址，请联系管理员补充");
        SysAttendanceRulePositionEntity attendanceRulePosition = null;
        if (attendanceRulePositionList.size() > 1) {
            double preVal = 0;
            for (SysAttendanceRulePositionEntity position : attendanceRulePositionList) {
                double distance = DistanceUtil.distance(position.getLongitude().doubleValue(), position.getLatitude().doubleValue(),
                        recordDTO.getClockLongitude(), recordDTO.getClockLatitude());
                if (distance == 0) {
                    attendanceRulePosition = position;
                    break;
                }
                if (preVal == 0 || preVal >= distance) {
                    preVal = distance;
                    attendanceRulePosition = position;
                }
            }
        } else {
            attendanceRulePosition = attendanceRulePositionList.get(0);
        }
        double distance = DistanceUtil.distance(attendanceRulePosition.getLongitude().doubleValue(), attendanceRulePosition.getLatitude().doubleValue(),
                recordDTO.getClockLongitude(), recordDTO.getClockLatitude());
        //异地打卡
        AttendanceStatusEnum attendanceStatus = AttendanceStatusEnum.NORMAL_ATTENDANCE;
        InformationDO userInformation = informationService.getInformationByUserId((long) userId);
        if (ObjectUtil.isNull(userInformation))
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.INFORMATION_NOT_EXISTS);

        if (distance > attendanceRulePosition.getRange()) {
            if (attendanceRule.getOffsiteClockState() == OffsiteClockStateEnum.FORBID_OFFSITE && !userInformation.getIsOffsiteAttendance())
                throw new BusinessException("请在考勤范围内打卡");
            attendanceStatus = AttendanceStatusEnum.OFFSITE_ATTENDANCE;
        }
        // 现在前端默认都允许离线打卡
        if (!recordDTO.isOnline()) {
            attendanceStatus = AttendanceStatusEnum.OFFLINE_ATTENDANCE;
        }

        // 查重需要分项目
        SysAttendanceRecordEntity attendanceRecord = mapper.getAttendanceRecordByUserIdAndUnixDate(userId, attendanceDate, recordDTO.getProjectId());
        // 没有考勤记录就说明今日还未打卡
        if (attendanceRecord == null) {
            attendanceRecord = new SysAttendanceRecordEntity();
            attendanceRecord.setDeptId(attendanceRule.getDeptId());
            attendanceRecord.setProjectId(attendanceRule.getProjectId());
            attendanceRecord.setUserId(userId);
            attendanceRecord.setAttendanceType(attendanceRule.getAttendanceType());
            attendanceRecord.setFlag(ENABLED.value());
        }
        AttendanceClockStatusEnum clockStatus = AttendanceClockStatusEnum.NOT_CLOCK;
        if (attendanceRule.getAttendanceType() == AttendanceTypeEnum.FIXED_ATTENDANCE) {
            // 固定考勤才记录下班卡，如果是新纪录则处理为上班卡，如果不是新纪录则处理为下班卡
            if (attendanceRecord.getId() == 0) {
                attendanceRecord.setClockInTime(clockDateTime);
                attendanceRecord.setClockInPosition(recordDTO.getClockPosition());
                attendanceRecord.setClockInLongitudeLatitude(recordDTO.getClockLongitude() + "," + recordDTO.getClockLatitude());
                clockStatus = attendanceRule.getClockInTime().compareTo(clockTime) >= 0 ? AttendanceClockStatusEnum.NORMAL_CLOCK : AttendanceClockStatusEnum.LATE_CLOCK;
                attendanceRecord.setClockInStatus(clockStatus);
                attendanceRecord.setClockInPhotoUrl(recordDTO.getPhotoUrl());
                // 固定考勤默认需要打两次卡
                attendanceRecord.setClockOffStatus(AttendanceClockStatusEnum.NOT_CLOCK);
            } else {
                attendanceRecord.setClockOffTime(clockDateTime);
                attendanceRecord.setClockOffPosition(recordDTO.getClockPosition());
                attendanceRecord.setClockOffLongitudeLatitude(recordDTO.getClockLongitude() + "," + recordDTO.getClockLatitude());
                clockStatus = attendanceRule.getClockOffTime().compareTo(clockTime) > 0 ? AttendanceClockStatusEnum.EARLY_CLOCK : AttendanceClockStatusEnum.NORMAL_CLOCK;
                attendanceRecord.setClockOffStatus(clockStatus);
                attendanceRecord.setClockOffPhotoUrl(recordDTO.getPhotoUrl());
            }
        }
        // 自由考勤只会记录上班卡
        if (attendanceRule.getAttendanceType() == AttendanceTypeEnum.FREEDOM_ATTENDANCE) {
            attendanceRecord.setClockInTime(clockDateTime);
            attendanceRecord.setClockInPosition(recordDTO.getClockPosition());
            attendanceRecord.setClockInLongitudeLatitude(recordDTO.getClockLongitude() + "," + recordDTO.getClockLatitude());
            attendanceRecord.setClockInStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
            attendanceRecord.setClockInPhotoUrl(recordDTO.getPhotoUrl());
        }
        attendanceRecord.setAttendanceStatus(attendanceStatus);
        attendanceRecord.setAttendanceRuleId(attendanceRule.getId());
        attendanceRecord.setAttendanceDate(attendanceDate);
        attendanceRecord.setAttendanceDateStr(attendanceDateStr);
        attendanceRecord.setCreatedTime(unixTime());
        attendanceRecord.setCreatedBy(userId);
        mapper.saveAttendanceRecord(attendanceRecord);

        log.info("userId: {}, userName: {}, distance: {}, attendanceStatus: {}, clockStatus: {}", userId, attendanceRecord.getUserName(), distance, attendanceStatus.getVal(), clockStatus.getVal());
        return resultMessage;
    }

    @Override
    public List<SysAttendanceRecordEntity> getDefaultAttendanceRecordList(Long deptId, Long projectId, Long userId,
                                                                          Integer unixStartTime, Integer unixEndTime,
                                                                          Pageable pageable) {
        log.info("分页查询考勤记录，deptId:{}, projectId:{}, userId:{}, startTime:{}, endTime:{}, pageable:{}",
                deptId, projectId, userId, unixStartTime, unixEndTime, JsonUtils.toJsonString(pageable));
        Set<Long> userIds = userService.getContainUserIds(projectId, deptId, userId);
        if ((Objects.nonNull(deptId) || Objects.nonNull(userId)) && userIds.isEmpty()) {
            return Collections.emptyList();
        }
        String userIdsStr = "";
        if (!userIds.isEmpty()) {
            userIdsStr = "(" + Joiner.on(",").join(userIds) + ")";
        }
        List<SysAttendanceRecordEntity> recordList = mapper.getAttendanceRecordByParamsV2(projectId, userIdsStr, unixStartTime, unixEndTime, pageable);
        if (recordList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> recordUserIds = recordList
                .stream()
                .map(SysAttendanceRecordEntity::getUserId).collect(Collectors.toSet());
        List<AdminUserDO> userList = adminUserMapper.selectBatchIds(recordUserIds);
        Map<Long, AdminUserDO> userNameMap = userList
                .stream()
                .collect(Collectors.toMap(AdminUserDO::getId, Function.identity()));
        List<DeptDO> deptList = deptService.getDeptList(userList
                .stream()
                .map(AdminUserDO::getDeptId)
                .collect(Collectors.toSet()));
        Map<Long, String> deptNameMap = deptList
                .stream()
                .collect(Collectors.toMap(DeptDO::getId, DeptDO::getName));
        Set<Integer> projectIds = recordList
                .stream()
                .map(SysAttendanceRecordEntity::getProjectId)
                .collect(Collectors.toSet());
        Map<Integer, String> projectNameMap = projectService.getProjectList(projectIds)
                .stream()
                .collect(Collectors.toMap(ProjectDO::getId, ProjectDO::getName));
        recordList.forEach(record -> {
            AdminUserDO userDO = userNameMap.get((long) record.getUserId());
            if (Objects.isNull(userDO)) {
                return;
            }
            record.setUserName(userDO.getNickname());
            record.setDeptName(deptNameMap.get(userDO.getDeptId()));
            record.setProjectName(projectNameMap.get(record.getProjectId()));
        });
        return recordList;
    }

    @Override
    public int getDefaultAttendanceRecordCount(Long deptId, Long projectId, Long userId, Integer unixStartTime, Integer unixEndTime) {
        log.info("分页查询考勤记录总条数，deptId:{}, projectId:{}, userId:{}, startTime:{}, endTime:{}",
                deptId, projectId, userId, toDateFormat(unixStartTime), toDateFormat(unixEndTime));
        Set<Long> userIds = userService.getContainUserIds(projectId, deptId, userId);
        if ((Objects.nonNull(deptId) || Objects.nonNull(userId)) && userIds.isEmpty()) {
            return 0;
        }
        String userIdsStr = "";
        if (!userIds.isEmpty()) {
            userIdsStr = "(" + Joiner.on(",").join(userIds) + ")";
        }
        return mapper.getAttendanceRecordCountByParams(projectId, userIdsStr, unixStartTime, unixEndTime);
    }

    @Override
    public SysAttendanceRecordEntity getAttendanceRecordByUserIdAndUnixDate(int userId, int attendanceDate, int projectId) {
        if (attendanceDate == 0)
            attendanceDate = unixTime(DateUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN));
        return mapper.getAttendanceRecordByUserIdAndUnixDate(userId, attendanceDate, projectId);
    }

    @Override
    public InfoEntity getAttendanceStatisticPageV2(Long deptId, Long projectId, Long userId, Integer attendanceMonth, Integer pageNo, Integer pageSize) {
        InfoEntity infoEntity = new InfoEntity();
        DateTime dateTime = DateUtil.lastMonth();
        // 默认查询上个月的数据，因为考勤一般在月底或者月初用
        int unixStartTime;
        int unixEndTime;
        if (Objects.nonNull(attendanceMonth) && !Objects.equals(0, attendanceMonth)) {
            dateTime = DateUtil.parse(toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
        } else {
            Date previousMonth = TimeUtils.parseAsDate(TimeUtils.plusMonths(TimeUtils.formatAsYYYYMM(new Date()), -1), "yyyyMM");
            // 用不上，只是为了idea不报黄- -!
            if (Objects.isNull(previousMonth)) {
                previousMonth = new Date();
            }
            attendanceMonth = (int) (previousMonth.getTime() / 1000);
        }
        // 获取查询开始时间和结束时间
        unixStartTime = unixTime(DateUtil.beginOfMonth(dateTime));
        unixEndTime = unixTime(DateUtil.endOfMonth(dateTime));
        log.info("获取考勤统计-得到开始时间和结束时间，attendanceMonth: {}, unixStartTime:{}, unixEndTime:{}", attendanceMonth, unixStartTime, unixEndTime);

        // 获取几个关联用户条件的最终用户ids交集
        Set<Long> userIds = userService.getContainUserIds(projectId, deptId, userId);
        if ((Objects.nonNull(projectId) || Objects.nonNull(deptId) || Objects.nonNull(userId)) && userIds.isEmpty()) {
            infoEntity.setList(Collections.emptyList());
            infoEntity.setTotal(0);
            return infoEntity;
        }
        log.info("获取考勤统计-得到用户ids，userIds:{}", userIds);

        // 获取用户和项目的关联关系
        List<UserProjectRuleSameRelation> userProjectSameRelationList = userProjectRuleSameRelationService.selectHireUserPage(
                userIds,
                TimeUtils.formatAsDate(TimeTransUtils.transSecondTimestamp2Date(unixEndTime)),
                pageNo, pageSize);
        if (userProjectSameRelationList.isEmpty()) {
            infoEntity.setList(Collections.emptyList());
            infoEntity.setTotal(0);
            return infoEntity;
        }
        Integer total = userProjectRuleSameRelationService.selectHireUserCount(
                userIds,
                TimeUtils.formatAsDate(TimeTransUtils.transSecondTimestamp2Date(unixEndTime)));

        // 获取用户对应的部门、项目、规则信息
        Set<Long> recordUserIds = userProjectSameRelationList
                .stream()
                .map(UserProjectRuleSameRelation::getUserId).collect(Collectors.toSet());
        List<AdminUserDO> userList = adminUserMapper.selectBatchIds(recordUserIds);
        Map<Long, AdminUserDO> userMap = userList
                .stream()
                .collect(Collectors.toMap(AdminUserDO::getId, Function.identity()));
        List<DeptDO> deptList = deptService.getDeptList(userList
                .stream()
                .map(AdminUserDO::getDeptId)
                .collect(Collectors.toSet()));
        Map<Long, String> deptIdAndNameMap = deptList
                .stream()
                .collect(Collectors.toMap(DeptDO::getId, DeptDO::getName));
        List<InformationDO> informationDOS = informationService.selectListByUserIds(recordUserIds);
        Map<Long, InformationDO> informationMap = informationDOS
                .stream()
                .collect(Collectors.toMap(InformationDO::getUserId, Function.identity()));
        Set<Integer> projectIds = userProjectSameRelationList
                .stream()
                .map(relation -> Arrays.stream(relation.getSameRuleProjectIds().split(",")).map(Integer::valueOf).collect(Collectors.toSet()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Map<Integer, String> projectIdAndNameMap = projectService.getProjectList(projectIds)
                .stream()
                .collect(Collectors.toMap(ProjectDO::getId, ProjectDO::getName));
        Map<Integer, SysAttendanceRuleEntity> projectRuleMap = sysAttendanceRuleService.getAttendanceRuleByProjectIds(projectIds)
                .stream()
                .collect(Collectors.toMap(SysAttendanceRuleEntity::getProjectId, Function.identity()));
        List<SysAttendanceRuleHoliday> holidayList = sysAttendanceRuleHolidayService.queryListByRuleIds(projectRuleMap.values()
                .stream()
                .map(entity -> (long) entity.getId())
                .collect(Collectors.toSet()));
        Map<Long, List<SysAttendanceRuleHoliday>> holidayMapByRuleId = holidayList
                .stream()
                .collect(Collectors.groupingBy(SysAttendanceRuleHoliday::getSysAttendanceRuleId));

        // 获取用户对应的流程统计时间
        Map<Long, Set<String>> userIdAndSameProjectIdsMap = userProjectSameRelationList
                .stream()
                .collect(
                        Collectors.groupingBy(
                                UserProjectRuleSameRelation::getUserId,
                                Collectors.mapping(UserProjectRuleSameRelation::getSameRuleProjectIds, Collectors.toSet())));
        Map<Long, Set<Integer>> userIdAndProjectIdMap = new HashMap<>();
        userIdAndSameProjectIdsMap.forEach((projectUserId, sameProjectIds) -> {
            Set<Integer> userProjectIds = sameProjectIds
                    .stream()
                    .map(sameProjectIdsStr -> Arrays.stream(sameProjectIdsStr.split(",")).map(Integer::valueOf).collect(Collectors.toSet()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            userIdAndProjectIdMap.put(projectUserId, userProjectIds);
        });
        AttendanceStatisticInfoTo leaveInfoTo = sysAttendanceRecordStatisticsService.getUserAndProjectInstanceInfoTo(
                userIdAndProjectIdMap, attendanceMonth, "leave", "DAY");
        AttendanceStatisticInfoTo addInfoTo = sysAttendanceRecordStatisticsService.getUserAndProjectInstanceInfoTo(
                userIdAndProjectIdMap, attendanceMonth, "add", "HOURS");
        AttendanceStatisticInfoTo travelInfoTo = sysAttendanceRecordStatisticsService.getUserAndProjectInstanceInfoTo(
                userIdAndProjectIdMap, attendanceMonth, "travel", "DAY");
        AttendanceStatisticInfoTo explainInfoTo = sysAttendanceRecordStatisticsService.getUserAndProjectInstanceInfoTo(
                userIdAndProjectIdMap, attendanceMonth, "explain", "DAY");

        // 获取本月所有天数数据
        Date monthDate = TimeTransUtils.transSecondTimestamp2Date(attendanceMonth);
        List<HolidayInfoDO> dayList = holidayInfoService.getDayListByMonth(monthDate);
        // 获取用户近两个月的打卡时间记录
        Map<Integer, Map<Integer, List<Long>>> recentlyMonthAttendanceDateList = this.getRecentlyMonthAttendanceDateList(new SysAttendanceRecordQueryParam(recordUserIds, attendanceMonth));

        Date today = new Date();
        Date monthStartDate = TimeUtils.getMonthStartDate(today);
        Date endDay;
        // 如果查询的是本月，则从昨天查起，因为今天可能还未打卡
        if (Objects.equals(TimeUtils.getMonthStartDate(monthDate), monthStartDate)) {
            endDay = TimeUtils.getDayEnd(TimeUtils.plusDays(today, -1));
        }
        // 如果不是查本月数据，直接查到月底
        else {
            endDay = TimeUtils.getMonthEndDate(monthDate);
        }
        List<HolidayInfoDO> yearDayList = holidayInfoService.getDayListByMonth(endDay);

        int endTimestamp = new BigDecimal(endDay.getTime()).divide(new BigDecimal(1000), 0, RoundingMode.DOWN).intValue();
        // 获取用户对应的时间内所有打卡记录数据
        String recordUserIdsStr = "(" + Joiner.on(",").join(recordUserIds) + ")";
        List<SysAttendanceRecordEntity> records = mapper.getAttendanceRecordByParamsV2(null, recordUserIdsStr, unixStartTime, endTimestamp, null);
        // 按用户维度分组的打卡记录
        Map<Integer, List<SysAttendanceRecordEntity>> groupRecordByUserId = records
                .stream()
                .collect(Collectors.groupingBy(SysAttendanceRecordEntity::getUserId));

        List<AttendanceStatisticInfoVo> statisticInfoVoList = new ArrayList<>();
        for (UserProjectRuleSameRelation projectRuleSameRelation : userProjectSameRelationList) {
            Long projectUserId = projectRuleSameRelation.getUserId();
            List<Long> sameProjectIds = Arrays.stream(projectRuleSameRelation.getSameRuleProjectIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
            Long firstProjectId = sameProjectIds.get(0);
            AdminUserDO user = userMap.get(projectUserId);
            if (Objects.isNull(user)) {
                continue;
            }
            InformationDO informationDO = informationMap.get(projectUserId);
            if (Objects.isNull(informationDO)) {
                continue;
            }
            // 如果有多个项目，说明这些项目的考勤规则一致，只需取第一个就能取到具体的规则
            SysAttendanceRuleEntity rule = projectRuleMap.get(firstProjectId.intValue());
            if (Objects.isNull(rule)) {
                continue;
            }
            // 请假（天）
            Map<Integer, BigDecimal> projectIdAndLeaveHoursMap = leaveInfoTo.getUserAndProjectWorkTime()
                    .getOrDefault(projectUserId.intValue(), Collections.emptyMap());
            // 请假实例
            Map<Integer, List<BpmProcessInstanceTo>> projectIdAndProcessListMap = leaveInfoTo.getUserAndProjectProcessMap()
                    .getOrDefault(projectUserId.intValue(), Collections.emptyMap());
            // 加班（小时）
            Map<Integer, BigDecimal> projectIdAndAddHoursMap = addInfoTo.getUserAndProjectWorkTime()
                    .getOrDefault(projectUserId.intValue(), Collections.emptyMap());
            // 出差（天）
            Map<Integer, BigDecimal> projectIdAndTravelHoursMap = travelInfoTo.getUserAndProjectWorkTime()
                    .getOrDefault(projectUserId.intValue(), Collections.emptyMap());
            // 考勤补充说明列表表单
            Map<Integer, List<Map<String, Object>>> projectIdAndExplainMap = explainInfoTo.getUserAndProjectProcessFormMap()
                    .getOrDefault(projectUserId.intValue(), Collections.emptyMap());

            // 按照工作星期数和是否同步节假日，过滤出考勤天数，从入职时间开始统计起
            Date hireDate = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(informationDO.getHireDate()));
            List<SysAttendanceRuleHoliday> holidays = holidayMapByRuleId.get((long) rule.getId());
            List<HolidayInfoDO> workDayList = SysAttendanceUtil.getWorkDayList(dayList, rule, holidays, hireDate, endDay);
            // 出勤日期时间戳（去重）
            Set<Integer> attendanceDateSet = new HashSet<>();
            // 迟到（次）
            int attendanceLateCount = 0;
            // 早退（次）
            int attendanceEarlyCount = 0;
            // 旷工（天）
            int attendanceAbsentCount = 0;
            // 漏签（次）
            int attendanceMissCount = 0;
            // 请假（天）
            BigDecimal attendanceLeaveCount = BigDecimal.ZERO;
            // 加班（小时）
            BigDecimal attendanceAddCount = BigDecimal.ZERO;
            // 出差（天）
            BigDecimal attendanceTravelCount = BigDecimal.ZERO;
            // 项目名称列表
            List<String> projectNameList = new LinkedList<>();

            // 根据考勤记录统计出迟到早退等次数数据
            List<SysAttendanceRecordEntity> recordListByUserId = groupRecordByUserId.getOrDefault(projectUserId.intValue(), Collections.emptyList());
            // 获取该项目在时间范围内的打卡记录
            Map<Integer, List<Long>> projectIdAndAttendanceTimestampMap = recentlyMonthAttendanceDateList.getOrDefault(projectUserId.intValue(), Collections.emptyMap());
            // 循环通用规则日期，组装项目级别数据
            for (Long sameProjectId : sameProjectIds) {
                // 获取当前用户在当前项目下的流程业务时间总数
                BigDecimal leaveDays = projectIdAndLeaveHoursMap.getOrDefault(sameProjectId.intValue(), BigDecimal.ZERO);
                attendanceLeaveCount = attendanceLeaveCount.add(leaveDays);
                BigDecimal addHours = projectIdAndAddHoursMap.getOrDefault(sameProjectId.intValue(), BigDecimal.ZERO);
                attendanceAddCount = attendanceAddCount.add(addHours);
                BigDecimal travelDays = projectIdAndTravelHoursMap.getOrDefault(sameProjectId.intValue(), BigDecimal.ZERO);
                attendanceTravelCount = attendanceTravelCount.add(travelDays);

                // 当前用户在当前项目下的请假记录
                List<BpmProcessInstanceTo> processList = projectIdAndProcessListMap.getOrDefault(sameProjectId.intValue(), Collections.emptyList());
                Set<String> leaveDateList = supplementUserProjectAttendanceRecordAndGetLeaveDateList(recordListByUserId, processList,
                        rule, sameProjectId, yearDayList);

                // 用考勤补充说明记录和请假记录覆盖原纪录，得出最终打卡记录
                List<Map<String, Object>> optionFormListMap = projectIdAndExplainMap.get(sameProjectId.intValue());
                this.supplementUserProjectAttendanceRecordByExplainInstance(recordListByUserId, projectIdAndAttendanceTimestampMap,
                        optionFormListMap, rule, sameProjectId);

                List<SysAttendanceRecordEntity> userAndProjectRecordList = recordListByUserId
                        .stream()
                        .filter(entity -> Objects.equals(sameProjectId.intValue(), entity.getProjectId()))
                        .collect(Collectors.toList());
                // 获取所有相似项目内的去重出勤天数
                attendanceDateSet.addAll(userAndProjectRecordList
                        .stream()
                        .map(SysAttendanceRecordEntity::getAttendanceDate)
                        .collect(Collectors.toSet()));

                // 固定考勤才存在迟到早退和漏签的情况
                if (!userAndProjectRecordList.isEmpty() && !rule.isFreedomRule()) {
                    attendanceLateCount += (int) userAndProjectRecordList
                            .stream()
                            .filter(entity -> Objects.equals(AttendanceClockStatusEnum.LATE_CLOCK, entity.getClockInStatus()))
                            .count();
                    attendanceEarlyCount += (int) userAndProjectRecordList
                            .stream()
                            .filter(entity -> Objects.equals(AttendanceClockStatusEnum.EARLY_CLOCK, entity.getClockOffStatus()))
                            .count();
                    // 漏签：自由打卡没有漏签，固定打卡 = 已打卡记录内的未打卡次数
                    attendanceMissCount += (int) userAndProjectRecordList
                            .stream()
                            .filter(entity -> Objects.equals(AttendanceClockStatusEnum.NOT_CLOCK, entity.getClockOffStatus()))
                            .count();
                }

                if (!workDayList.isEmpty()) {
                    // 获取到当前用户当前月当前项目有哪些该打卡的天没有打卡
                    Set<String> projectAttendanceStrList = recordListByUserId
                            .stream()
                            .map(entity -> entity.getAttendanceDateStr().replace("-", ""))
                            .collect(Collectors.toSet());
                    // 未打卡的和请假记录都没有的记录，才能算是旷工
                    List<Date> notAttendanceDayList = workDayList
                            .stream()
                            .filter(day -> !projectAttendanceStrList.contains(day.getDate().toString())
                                    && !leaveDateList.contains(day.getDate().toString()))
                            .map(day -> TimeUtils.parseAsDate(day.getDate().toString(), "yyyyMMdd"))
                            .collect(Collectors.toList());

                    // 如果打卡记录是空的，或者只在一个项目内打了卡，则直接计算，避免新员工不打卡就看不见旷工数据的情况
                    if (projectIdAndAttendanceTimestampMap.keySet().size() <= 1) {
                        Set<Integer> userProjectIds = userIdAndProjectIdMap.getOrDefault(projectUserId, Collections.emptySet());
                        Integer finalProjectId = userProjectIds.stream().max(Integer::compareTo).orElse(0);
                        if (Objects.equals(finalProjectId, sameProjectId.intValue())) {
                            attendanceAbsentCount += notAttendanceDayList.size();
                        }
                    } else {
                        // 如果在多个项目内打了卡，查这些未打卡天之前的最近一次打卡记录是在哪个项目上，优先查之前的，在哪个项目上就算哪个项目的
                        for (Date date : notAttendanceDayList) {
                            int finalProjectId = SysAttendanceRecordUtil.getFinalProjectIdByAttendanceRecordMap(projectIdAndAttendanceTimestampMap, date);
                            if (Objects.equals(finalProjectId, sameProjectId.intValue())) {
                                attendanceAbsentCount += 1;
                            }
                        }
                    }
                }

                String projectName = projectIdAndNameMap.get(sameProjectId.intValue());
                if (StringUtils.isNotBlank(projectName)) {
                    projectNameList.add(projectName);
                }
            }
            AttendanceStatisticInfoVo statisticInfoVo = new AttendanceStatisticInfoVo();
            // 装载统计信息
            statisticInfoVo.setUserId(projectUserId.intValue());
            statisticInfoVo.setUserName(user.getNickname());
            statisticInfoVo.setDeptId(user.getDeptId().intValue());
            statisticInfoVo.setDeptName(deptIdAndNameMap.get(user.getDeptId()));
            statisticInfoVo.setProjectName(Joiner.on(",").join(projectNameList));
            statisticInfoVo.setHireDateStr(TimeUtils.formatAsDate(informationDO.getHireDate()));
            statisticInfoVo.setAttendanceMonth(dateTime.month() + 1);
            statisticInfoVo.setAttendanceMonthDayCount(workDayList.size());
            statisticInfoVo.setAttendanceNormalCount(attendanceDateSet.size());
            statisticInfoVo.setAttendanceLateCount(Math.max(attendanceLateCount, 0));
            statisticInfoVo.setAttendanceEarlyCount(Math.max(attendanceEarlyCount, 0));
            statisticInfoVo.setAttendanceMissCount(Math.max(attendanceMissCount, 0));
            statisticInfoVo.setAttendanceAbsentCount(Math.max(attendanceAbsentCount, 0));
            statisticInfoVo.setAttendanceLeaveCount(attendanceLeaveCount);
            statisticInfoVo.setAttendanceAddCount(attendanceAddCount);
            statisticInfoVo.setAttendanceTravelCount(attendanceTravelCount);
            statisticInfoVoList.add(statisticInfoVo);
        }
        infoEntity.setList(statisticInfoVoList);
        infoEntity.setTotal(total);
        return infoEntity;
    }

    /**
     * 根据考勤补充说明流程补充打卡记录
     *
     * @param recordListByUserId                 用户在各个项目下的单月打卡记录列表
     * @param projectIdAndAttendanceTimestampMap 用户在各个项目下的近两个月打卡时间列表
     * @param optionFormListMap                  考勤补充说明流程表单列表
     * @param rule                               考勤规则
     * @param sameProjectId                      项目id
     */
    private void supplementUserProjectAttendanceRecordByExplainInstance(List<SysAttendanceRecordEntity> recordListByUserId,
                                                                        Map<Integer, List<Long>> projectIdAndAttendanceTimestampMap,
                                                                        List<Map<String, Object>> optionFormListMap,
                                                                        SysAttendanceRuleEntity rule, Long sameProjectId) {
        if (CollectionUtils.isNotEmpty(optionFormListMap)) {
            optionFormListMap.forEach(optionForm -> {
                String option = MapUtils.getString(optionForm, ATTENDANCE_EXPLAIN_OPTION_COLUMN_NAME);
                if (StringUtils.isEmpty(option)) {
                    return;
                }
                // 考勤异常日期
                Long attendanceAbnormalTimestamp = MapUtils.getLong(optionForm, "date2");
                if (Objects.isNull(attendanceAbnormalTimestamp)) {
                    return;
                }
                String attendanceAbnormalDateStr = TimeUtils.formatAsDate(TimeTransUtils.transSecondTimestamp2Date(attendanceAbnormalTimestamp));
                // 实际到岗时间
                Date attendanceActualClockInDate = null;
                String attendanceActualClockInTimeStr = MapUtils.getString(optionForm, "date3");
                if (StringUtils.isNotBlank(attendanceActualClockInTimeStr)) {
                    // 有可能是配的字符串，比如08:00这种，所以要分开处理
                    if (!StringUtils.isNumeric(attendanceActualClockInTimeStr)) {
                        attendanceActualClockInDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + attendanceActualClockInTimeStr + ":00");
                    } else {
                        attendanceActualClockInTimeStr = TimeUtils.formatAsTime(TimeTransUtils.transSecondTimestamp2Date(Long.parseLong(attendanceActualClockInTimeStr)));
                        attendanceActualClockInDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + attendanceActualClockInTimeStr);
                    }
                }
                // 实际离岗时间
                Date attendanceActualClockOffDate = null;
                String attendanceActualClockOffTimeStr = MapUtils.getString(optionForm, "date4");
                if (StringUtils.isNotBlank(attendanceActualClockOffTimeStr)) {
                    if (!StringUtils.isNumeric(attendanceActualClockOffTimeStr)) {
                        attendanceActualClockOffDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + attendanceActualClockOffTimeStr + ":00");
                    } else {
                        // 具体的时间戳的日期是不准的，需要单独解时间来拼接
                        attendanceActualClockOffTimeStr = TimeUtils.formatAsTime(TimeTransUtils.transSecondTimestamp2Date(Long.parseLong(attendanceActualClockOffTimeStr)));
                        attendanceActualClockOffDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + attendanceActualClockOffTimeStr);
                    }
                }

                // 获取对应的考勤记录
                List<SysAttendanceRecordEntity> attendanceAbnormalRecordList = recordListByUserId
                        .stream()
                        .filter(record -> Objects.equals(attendanceAbnormalDateStr, record.getAttendanceDateStr()))
                        .collect(Collectors.toList());

                // 只有固定考勤才会有迟到早退和漏签
                if (!rule.isFreedomRule() && !attendanceAbnormalRecordList.isEmpty() && !Objects.equals(option, AttendanceExplainProcessOptionEnum.ABSENT.getValue())) {
                    Date ruleClockInDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + rule.getClockInTime());
                    Date ruleClockOffDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + rule.getClockOffTime());
                    // 一个用户可能同一天在多个项目内打卡，这样的话多条数据都要处理
                    for (SysAttendanceRecordEntity attendanceRecord : attendanceAbnormalRecordList) {
                        // 就算是考勤补充，也有可能提交的时间不属于正常范围，所以还是要去做判断
                        if (Objects.nonNull(attendanceActualClockInDate)) {
                            if (attendanceActualClockInDate.after(ruleClockInDate)) {
                                attendanceRecord.setClockInStatus(AttendanceClockStatusEnum.LATE_CLOCK);
                            } else {
                                attendanceRecord.setClockInStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                            }
                        }
                        if (Objects.nonNull(attendanceActualClockOffDate)) {
                            if (attendanceActualClockOffDate.before(ruleClockOffDate)) {
                                attendanceRecord.setClockOffStatus(AttendanceClockStatusEnum.EARLY_CLOCK);
                            } else {
                                attendanceRecord.setClockOffStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                            }
                        }
                    }
                }
                // 如果是旷工，则补充一条数据进去，如果当天本来就有打卡记录，则忽略
                if (Objects.equals(option, AttendanceExplainProcessOptionEnum.ABSENT.getValue()) && attendanceAbnormalRecordList.isEmpty()) {
                    SysAttendanceRecordEntity supplementRecord = new SysAttendanceRecordEntity();
                    supplementRecord.setProjectId(sameProjectId.intValue());
                    supplementRecord.setAttendanceDateStr(attendanceAbnormalDateStr);
                    supplementRecord.setAttendanceDate(attendanceAbnormalTimestamp.intValue());
                    if (rule.isFreedomRule()) {
                        supplementRecord.setClockInStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                    } else {
                        Date ruleClockInDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + rule.getClockInTime());
                        Date ruleClockOffDate = TimeUtils.parseAsDateTime(attendanceAbnormalDateStr + " " + rule.getClockOffTime());
                        if (Objects.nonNull(attendanceActualClockInDate)) {
                            if (attendanceActualClockInDate.after(ruleClockInDate)) {
                                supplementRecord.setClockInStatus(AttendanceClockStatusEnum.LATE_CLOCK);
                            } else {
                                supplementRecord.setClockInStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                            }
                        }
                        if (Objects.nonNull(attendanceActualClockOffDate)) {
                            if (attendanceActualClockOffDate.before(ruleClockOffDate)) {
                                supplementRecord.setClockOffStatus(AttendanceClockStatusEnum.EARLY_CLOCK);
                            } else {
                                supplementRecord.setClockOffStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                            }
                        }
                    }
                    recordListByUserId.add(supplementRecord);

                    // 装载用户近几个月打卡记录的数据
                    if (Objects.nonNull(attendanceActualClockInDate)) {
                        List<Long> attendanceTimestampList = projectIdAndAttendanceTimestampMap.get(sameProjectId.intValue());
                        if (CollectionUtils.isEmpty(attendanceTimestampList)) {
                            attendanceTimestampList = new LinkedList<>();
                            projectIdAndAttendanceTimestampMap.put(sameProjectId.intValue(), attendanceTimestampList);
                        }
                        attendanceTimestampList.add(attendanceActualClockInDate.getTime());
                    }
                }
            });
        }
    }

    /**
     * 根据请假流程补充打卡记录
     *
     * @param recordListByUserId 用户在各个项目下的单月打卡记录列表
     * @param processList        请假流程集合
     * @param rule               考勤规则
     * @param sameProjectId      项目id
     * @param yearDayList        当前年所有日期列表
     */
    private Set<String> supplementUserProjectAttendanceRecordAndGetLeaveDateList(List<SysAttendanceRecordEntity> recordListByUserId,
                                                                                 List<BpmProcessInstanceTo> processList,
                                                                                 SysAttendanceRuleEntity rule, Long sameProjectId,
                                                                                 List<HolidayInfoDO> yearDayList) {
        Set<String> leaveDateStrList = new HashSet<>();
        List<Date> yearDateList = yearDayList
                .stream()
                .map(holiday -> TimeUtils.parseAsDate(holiday.getDate().toString(), "yyyyMMdd"))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(processList)) {
            processList.forEach(process -> {
                // 解出流程业务表单内的开始时间和结束时间
                Date formStartTime = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(process.getFormStartTime()));
                Date formEndTime = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(process.getFormEndTime()));
                String formStartDateStr = TimeUtils.formatAsDate(formStartTime);
                String formEndDateStr = TimeUtils.formatAsDate(formEndTime);
                if (Objects.isNull(formStartTime) || Objects.isNull(formEndTime)) {
                    return;
                }
                if (rule.isFreedomRule()) {
                    List<SysAttendanceRecordEntity> startTimeRecord = recordListByUserId
                            .stream()
                            .filter(record -> Objects.equals((int) formStartTime.getTime(), record.getAttendanceDate()))
                            .collect(Collectors.toList());
                    // 处理结束时间
                    List<SysAttendanceRecordEntity> endTimeRecord = recordListByUserId
                            .stream()
                            .filter(record -> Objects.equals((int) formEndTime.getTime(), record.getAttendanceDate()))
                            .collect(Collectors.toList());
                    // 自由考勤只需要校验是否请假天存在打卡记录，如果存在就不处理，不存在就加入请假时间列表内
                    if (startTimeRecord.isEmpty()) {
                        String formDateStr = TimeUtils.formatAsYYYYMMDD(formStartTime);
                        leaveDateStrList.add(formDateStr);
                    }
                    if (endTimeRecord.isEmpty()) {
                        String formDateStr = TimeUtils.formatAsYYYYMMDD(formEndTime);
                        leaveDateStrList.add(formDateStr);
                    }
                } else {
                    long daysBetween = TimeUtils.daysBetween(formStartTime, formEndTime);
                    // 有数据但没有上班打卡数据，补充上班打卡数据，有上班打卡数据不处理
                    // 处理开始时间
                    List<SysAttendanceRecordEntity> startTimeRecord = recordListByUserId
                            .stream()
                            .filter(record -> Objects.equals(formStartDateStr, record.getAttendanceDateStr()))
                            .collect(Collectors.toList());
                    SysAttendanceRecordEntity startRecord = this.supplementRecordListIfEmpty(startTimeRecord, sameProjectId,
                            formStartTime, recordListByUserId);
                    if (Objects.nonNull(startRecord) && Objects.isNull(startRecord.getClockInTime())) {
                        // 判断开始时间是否符合考勤规则
                        Date ruleClockInTime = TimeUtils.parseAsDateTime(formStartDateStr + " " + rule.getClockInTime());
                        if (Objects.isNull(ruleClockInTime)) {
                            return;
                        }
                        Date ruleClockOffTime = TimeUtils.parseAsDateTime(formStartDateStr + " " + rule.getClockOffTime());
                        if (Objects.isNull(ruleClockOffTime)) {
                            return;
                        }
                        if (formStartTime.getTime() <= ruleClockInTime.getTime()) {
                            startRecord.setClockInStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                        } else {
                            startRecord.setClockInStatus(AttendanceClockStatusEnum.LATE_CLOCK);
                        }
                        // 如果请假开始结束时间不在同一天，则正常化当天的结束时间
                        if (daysBetween > 0 && ruleClockOffTime.getTime() >= formStartTime.getTime()) {
                            startRecord.setClockOffStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                        }
                    }

                    // 有数据但没有上班打卡数据，补充上班打卡数据，有上班打卡数据不处理
                    List<SysAttendanceRecordEntity> endTimeRecord = recordListByUserId
                            .stream()
                            .filter(record -> Objects.equals(formEndDateStr, record.getAttendanceDateStr()))
                            .collect(Collectors.toList());
                    SysAttendanceRecordEntity recordEntity = this.supplementRecordListIfEmpty(endTimeRecord, sameProjectId,
                            formEndTime, recordListByUserId);
                    if (Objects.nonNull(recordEntity) && Objects.isNull(recordEntity.getClockOffTime())) {
                        // 判断开始时间是否符合考勤规则
                        Date ruleClockOffTime = TimeUtils.parseAsDateTime(formEndDateStr + " " + rule.getClockOffTime());
                        if (Objects.isNull(ruleClockOffTime)) {
                            return;
                        }
                        Date ruleClockInTime = TimeUtils.parseAsDateTime(formEndDateStr + " " + rule.getClockInTime());
                        if (Objects.isNull(ruleClockInTime)) {
                            return;
                        }
                        if (formEndTime.getTime() >= ruleClockOffTime.getTime()) {
                            recordEntity.setClockOffStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                        } else {
                            recordEntity.setClockOffStatus(AttendanceClockStatusEnum.EARLY_CLOCK);
                        }
                        // 如果请假开始结束时间不在同一天，且结束时间在正常打卡范围内，则正常化当天的上班打卡状态
                        if (daysBetween > 0 && ruleClockInTime.getTime() <= formEndTime.getTime()) {
                            recordEntity.setClockOffStatus(AttendanceClockStatusEnum.NORMAL_CLOCK);
                        }
                    }

                    // 如果间隔时间超过一天，则中间的数据全部补充成正常考勤数据
                    if (daysBetween > 1) {
                        Date formStartDate = TimeUtils.parseAsDate(formStartDateStr, "yyyy-MM-dd");
                        Date formEndDate = TimeUtils.parseAsDate(formEndDateStr, "yyyy-MM-dd");
                        // 获取开始和结束时间之间间隔的正常天
                        leaveDateStrList.addAll(yearDateList
                                .stream()
                                .filter(date -> date.after(formStartDate) && date.before(formEndDate))
                                .map(TimeUtils::formatAsYYYYMMDD)
                                .collect(Collectors.toList()));
                    }
                }
            });
        }
        return leaveDateStrList;
    }

    private SysAttendanceRecordEntity supplementRecordListIfEmpty(List<SysAttendanceRecordEntity> clockList, Long sameProjectId,
                                                                  Date formTime, List<SysAttendanceRecordEntity> recordListByUserId) {
        if (clockList.isEmpty()) {
            SysAttendanceRecordEntity supplementRecord = new SysAttendanceRecordEntity();
            supplementRecord.setProjectId(sameProjectId.intValue());
            String attendanceDateStr = TimeUtils.formatAsDate(formTime);
            Date attendanceDate = TimeUtils.parseAsDate(attendanceDateStr, "yyyy-MM-dd");
            if (Objects.isNull(attendanceDate)) {
                return null;
            }
            supplementRecord.setAttendanceDateStr(attendanceDateStr);
            long attendanceDatetime = TimeTransUtils.transMilliTimestamp2SecondTimestamp(attendanceDate.getTime());
            supplementRecord.setAttendanceDate((int) attendanceDatetime);
            supplementRecord.setClockInStatus(AttendanceClockStatusEnum.NOT_CLOCK);
            supplementRecord.setClockOffStatus(AttendanceClockStatusEnum.NOT_CLOCK);
            recordListByUserId.add(supplementRecord);
            return supplementRecord;
        } else {
            return clockList.get(0);
        }
    }

    @Override
    public void exportAttendanceRecordList(Long deptId, Long projectId, Long userId, Integer unixStartTime, Integer unixEndTime,
                                           HttpServletRequest req, HttpServletResponse resp) {
        List<SysAttendanceRecordEntity> recordList = this.getDefaultAttendanceRecordList(deptId, projectId, userId, unixStartTime, unixEndTime, null);
//        recordList = recordList.stream().sorted(Comparator.comparing(SysAttendanceRecordEntity::getUserId)).collect(Collectors.toList());
        List<AttendanceRecordExcelVo> rows = new ArrayList<>();
        for (SysAttendanceRecordEntity record : recordList) {
            AttendanceRecordExcelVo row = new AttendanceRecordExcelVo();
            BeanUtil.copyProperties(record, row);
            row.setAttendanceTypeVal(record.getAttendanceTypeVal());
            row.setAttendanceStatusVal(record.getAttendanceStatusVal());
            row.setClockInStatusVal(record.getClockInClockStatusVal());
            row.setClockOffStatusVal(record.getClockOffClockStatusVal());
            rows.add(row);
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("userName", "姓名");
        writer.addHeaderAlias("deptName", "部门");
        writer.addHeaderAlias("projectName", "项目");
        writer.addHeaderAlias("attendanceDateStr", "考勤日期");
        writer.addHeaderAlias("attendanceTypeVal", "考勤类型");
        writer.addHeaderAlias("attendanceStatusVal", "考勤方式");
        writer.addHeaderAlias("clockInTime", "签到时间");
        writer.addHeaderAlias("clockInStatusVal", "签到状态");
        writer.addHeaderAlias("clockInPosition", "签到地址");
        writer.addHeaderAlias("clockOffTime", "签退时间");
        writer.addHeaderAlias("clockOffStatusVal", "签退状态");
        writer.addHeaderAlias("clockOffPosition", "签退地址");
        writer.addHeaderAlias("clockInLongitudeLatitude", "签到坐标");
        writer.addHeaderAlias("clockOffLongitudeLatitude", "签退坐标");

        for (int i = 0; i < AttendanceRecordExcelVo.class.getDeclaredFields().length; i++) {
            writer.setColumnWidth(i, 16);
        }
        writer.setColumnWidth(0, 20);//姓名
        writer.setColumnWidth(1, 20);//部门
        writer.setColumnWidth(2, 32);//项目
        writer.setColumnWidth(7, 32);//签到位置
        writer.setColumnWidth(11, 32);//签退位置
        writer.setFreezePane(1);
        writer.write(rows);
        String filename = "考勤记录" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN) + ".xls";
        this.setFilenameHeader(req, resp, filename);
        try {
            OutputStream os = resp.getOutputStream();
            writer.flush(os, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.close();
        log.info("考勤记录导出成功：{}", filename);
    }

    @Override
    public void exportAttendanceRecordStatisticV2(Long deptId, Long projectId, Long userId, Integer attendanceMonth, HttpServletRequest req, HttpServletResponse resp) {
        try {
            InfoEntity infoEntity = this.getAttendanceStatisticPageV2(deptId, projectId, userId, attendanceMonth, 0, 0);
            List<AttendanceStatisticExcelVo> rows = new ArrayList<>();
            List<AttendanceStatisticInfoVo> statisticList = new ArrayList<>();
            if (infoEntity.getList() != null)
                statisticList = (List<AttendanceStatisticInfoVo>) infoEntity.getList();
            for (AttendanceStatisticInfoVo statisticInfo : statisticList) {
                AttendanceStatisticExcelVo row = new AttendanceStatisticExcelVo();
                BeanUtil.copyProperties(statisticInfo, row);
                row.setHireDateStr(toDateFormat(statisticInfo.getHireDate()));
                row.setAttendanceMonthStr(statisticInfo.getAttendanceMonth() + "月");
                rows.add(row);
            }
            String filename = "考勤统计" + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN) + ".xls";
            this.setFilenameHeader(req, resp, filename);
            EasyExcel.write(resp.getOutputStream(), AttendanceStatisticExcelVo.class).sheet("考勤").doWrite(rows);
            log.info("考勤统计导出成功：{}", filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AttendanceStatisticInfoVo> getAttendanceStatisticPageList(int deptId, int projectId, String
            userName, Integer attendanceMonth, int userId) {
        DateTime dateTime = DateUtil.date();
        int unixTimeNow = unixTime(dateTime);
        int unixStartTime;
        int unixEndTime;
        if (attendanceMonth == null || attendanceMonth == 0) {
            unixStartTime = unixTime(DateUtil.beginOfMonth(dateTime));
            unixEndTime = unixTimeNow;
        } else {
            dateTime = DateUtil.parse(toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
            unixStartTime = unixTime(DateUtil.beginOfMonth(dateTime));
            unixEndTime = unixTime(DateUtil.endOfMonth(dateTime));
        }
        log.info("attendanceMonth: {}", dateTime.toDateStr());

        List<AttendanceStatisticInfoVo> statisticInfoVoList = new ArrayList<>();
        List<InformationDO> informationDOS = null;

        InformationExportReqVO exportReqVO = new InformationExportReqVO();
        if (StrUtil.isNotBlank(userName))
            exportReqVO.setName(userName);
        if (userId > 0)
            exportReqVO.setUserId((long) userId);
        if (deptId > 0)
            exportReqVO.setDeptId((long) deptId);
        informationDOS = informationService.getInformationList(exportReqVO);

        if (ObjectUtil.isEmpty(informationDOS)) {
            return statisticInfoVoList;
        }

        for (InformationDO userDo : informationDOS) {
            AttendanceStatisticInfoVo statisticInfoVo = new AttendanceStatisticInfoVo();
            statisticInfoVo.setUserId(userDo.getUserId() == null ? 0 : Math.toIntExact(userDo.getUserId()));
            statisticInfoVo.setUserName(userDo.getName());
            statisticInfoVo.setDeptId(userDo.getDeptId() == null ? 0 : Math.toIntExact(userDo.getDeptId()));
            statisticInfoVo.setDeptName(userDo.getDeptName());
            statisticInfoVo.setHireDate((int) (LocalDateTimeUtil.toEpochMilli(userDo.getHireDate()) / 1000));
            statisticInfoVo.setAttendanceMonth(dateTime.month() + 1);
            statisticInfoVo.setAttendanceMonthDayCount(dateTime.getLastDayOfMonth());
            List<SysAttendanceRecordEntity> recordList = null;
            //考虑进场时间
            if (statisticInfoVo.getHireDate() > 0) {
                if (statisticInfoVo.getHireDate() > unixTimeNow)
                    continue;
                recordList = mapper.getAttendanceRecordList(deptId, projectId, statisticInfoVo.getUserId(),
                        Math.max(statisticInfoVo.getHireDate(), unixStartTime), Math.max(statisticInfoVo.getHireDate(), unixEndTime), null);
            } else {
                recordList = mapper.getAttendanceRecordList(deptId, projectId, statisticInfoVo.getUserId(),
                        unixStartTime, unixEndTime, null);
            }
            if (ObjectUtil.isEmpty(recordList)) {
                statisticInfoVoList.add(statisticInfoVo);
                continue;
            }
            Integer attendanceNormalCount = recordList.size();//出勤（天）
            Integer attendanceLateCount = 0;//迟到（次）
            Integer attendanceEarlyCount = 0;//早退（次）
            Integer attendanceMissCount = 0;//漏签（次）
            Double attendanceLeaveCount = 0.0;//请假（小时）
//            Integer attendanceAbsentCount = 0;//旷工（次）
            for (SysAttendanceRecordEntity record : recordList) {
                SysAttendanceRuleEntity rule = record.getAttendanceRule();
                if (rule.getAttendanceType() == AttendanceTypeEnum.FIXED_ATTENDANCE) {
                    if (record.getClockInStatus() == AttendanceClockStatusEnum.LATE_CLOCK)
                        attendanceLateCount++;
                    if (record.getClockOffStatus() == AttendanceClockStatusEnum.EARLY_CLOCK)
                        attendanceEarlyCount++;
                    if (record.getClockOffStatus() == AttendanceClockStatusEnum.NOT_CLOCK)
                        attendanceMissCount++;
                }
            }
            statisticInfoVo.setAttendanceNormalCount(attendanceNormalCount);
            statisticInfoVo.setAttendanceLateCount(attendanceLateCount);
            statisticInfoVo.setAttendanceEarlyCount(attendanceEarlyCount);
            statisticInfoVo.setAttendanceMissCount(attendanceMissCount);
            statisticInfoVo.setAttendanceLeaveCount(BigDecimal.valueOf(attendanceLeaveCount));
//            statisticInfoVo.setAttendanceAbsentCount(attendanceAbsentCount);
            statisticInfoVoList.add(statisticInfoVo);
        }
        return statisticInfoVoList;

    }

    @Override
    public List<AttendanceStatisticProjectInfoVo> getAttendanceRecordStatisticProject(int projectId, Integer
            attendanceMonth) {
        DateTime dateTime = DateUtil.date();
        int unixTimeNow = unixTime(dateTime);
        int unixStartTime;
        int unixEndTime;
        if (attendanceMonth == null || attendanceMonth == 0) {
            unixStartTime = unixTime(DateUtil.beginOfMonth(dateTime));
            unixEndTime = unixTimeNow;
        } else {
            dateTime = DateUtil.parse(toDateFormat(attendanceMonth), DatePattern.NORM_DATE_PATTERN);
            unixStartTime = unixTime(DateUtil.beginOfMonth(dateTime));
            unixEndTime = unixTime(DateUtil.endOfMonth(dateTime));
        }
        log.info("attendanceMonth: {}", dateTime.toDateStr());

        List<SysAttendanceRecordEntity> attendanceRecordByProject = mapper.getAttendanceRecordByProject(projectId, unixStartTime, unixEndTime, null);
        ProjectDO project = projectService.getProject(projectId);
        List<Integer> UserIds = attendanceRecordByProject.stream().map(SysAttendanceRecordEntity::getUserId).distinct().collect(Collectors.toList());


        List<AttendanceStatisticProjectInfoVo> statisticProjectInfoVoList = new ArrayList<>();
        List<InformationDO> informationDOS = new ArrayList<>();
        int total = 0;
        for (Integer userId : UserIds) {
            InformationDO informationByUserId = informationService.getInformationByUserId((long) userId);
            informationDOS.add(informationByUserId);
            total = informationDOS.size();
        }
        if (ObjectUtil.isEmpty(informationDOS)) {
            return statisticProjectInfoVoList;
        }

        for (InformationDO userDo : informationDOS) {
            AttendanceStatisticProjectInfoVo statisticProjectInfoVo = new AttendanceStatisticProjectInfoVo();
            statisticProjectInfoVo.setUserId(userDo.getUserId() == null ? 0 : Math.toIntExact(userDo.getUserId()));
            statisticProjectInfoVo.setUserName(userDo.getName());
            statisticProjectInfoVo.setProjectId(projectId);
            statisticProjectInfoVo.setProjectName(project.getName());
            statisticProjectInfoVo.setHireDate((int) (LocalDateTimeUtil.toEpochMilli(userDo.getHireDate()) / 1000));
            statisticProjectInfoVo.setAttendanceMonth(dateTime.month() + 1);
            statisticProjectInfoVo.setAttendanceMonthDayCount(dateTime.getLastDayOfMonth());
            List<SysAttendanceRecordEntity> recordList = null;
            //考虑进场时间
            if (statisticProjectInfoVo.getHireDate() > 0) {
                if (statisticProjectInfoVo.getHireDate() > unixTimeNow)
                    continue;
                recordList = mapper.getAttendanceRecordList(0, projectId, statisticProjectInfoVo.getUserId(),
                        Math.max(statisticProjectInfoVo.getHireDate(), unixStartTime), Math.max(statisticProjectInfoVo.getHireDate(), unixEndTime), null);
            } else {
                recordList = mapper.getAttendanceRecordList(0, projectId, statisticProjectInfoVo.getUserId(),
                        unixStartTime, unixEndTime, null);
            }
            if (ObjectUtil.isEmpty(recordList)) {
                statisticProjectInfoVoList.add(statisticProjectInfoVo);
                continue;
            }
            Integer attendanceNormalCount = recordList.size();//出勤（天）
            Integer attendanceLateCount = 0;//迟到（次）
            Integer attendanceEarlyCount = 0;//早退（次）
            Integer attendanceMissCount = 0;//漏签（次）
            Integer attendanceLeaveCount = 0;//请假（小时）
//            Integer attendanceAbsentCount = 0;//旷工（次）
            for (SysAttendanceRecordEntity record : recordList) {
                SysAttendanceRuleEntity rule = record.getAttendanceRule();
                if (rule.getAttendanceType() == AttendanceTypeEnum.FIXED_ATTENDANCE) {
                    if (record.getClockInStatus() == AttendanceClockStatusEnum.LATE_CLOCK)
                        attendanceLateCount++;
                    if (record.getClockOffStatus() == AttendanceClockStatusEnum.EARLY_CLOCK)
                        attendanceEarlyCount++;
                    if (record.getClockOffStatus() == AttendanceClockStatusEnum.NOT_CLOCK)
                        attendanceMissCount++;
                }
            }
            statisticProjectInfoVo.setAttendanceNormalCount(attendanceNormalCount);
            statisticProjectInfoVo.setAttendanceLateCount(attendanceLateCount);
            statisticProjectInfoVo.setAttendanceEarlyCount(attendanceEarlyCount);
            statisticProjectInfoVo.setAttendanceMissCount(attendanceMissCount);
            statisticProjectInfoVo.setAttendanceLeaveCount(attendanceLeaveCount);
//            statisticProjectInfoVo.setAttendanceAbsentCount(attendanceAbsentCount);
            statisticProjectInfoVoList.add(statisticProjectInfoVo);
        }
        return statisticProjectInfoVoList;
    }

    /**
     * 文件名编码
     */
    private static void setFilenameHeader(HttpServletRequest req, HttpServletResponse resp, String filename) {
        try {
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("multipart/form-data");
            resp.setHeader("Content-Disposition", "attachment;fileName=" + setFileDownloadHeader(req, filename));
        } catch (UnsupportedEncodingException e) {
            log.error("文件名编码出错", e);
            throw new BusinessException("文件编码出错");
        }
    }

    private static String setFileDownloadHeader(HttpServletRequest req, String fileName) throws
            UnsupportedEncodingException {
        final String agent = req.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 获取指定日期区间的全部日期
     */
    private List<String> getRangeDates(DateTime startDate, DateTime endDate) {
        List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
        return dateTimes.stream().map(DateTime::toDateStr).collect(Collectors.toList());
    }

    //TODO

    /**
     * 是否为节假日（考虑周末）
     */
    public boolean isLegalHoliday(String dataStr) {
        List<Map<String, Object>> fullYearDates = ReaderTxtFileUtil.readerTxt(oaLegalHolidayUrl);
        return fullYearDates.stream().anyMatch(f -> MapUtil.getInt(f, "status") == 0 && dataStr.contains(MapUtil.getStr(f, "date")));
    }

    /**
     * 获取当月工作日
     */
    public List<String> getWorkDate(List<String> dataStr) {
        List<Map<String, Object>> fullYearDates = ReaderTxtFileUtil.readerTxt(oaLegalHolidayUrl);
        return fullYearDates.stream()
                .filter(f -> MapUtil.getInt(f, "status") > 0 && dataStr.contains(MapUtil.getStr(f, "date")))
                .map(m -> MapUtil.getStr(m, "date")).collect(Collectors.toList());
    }

    /**
     * 获取当月所有日期
     */
    public List<String> getAllDate(List<String> dataStr) {
        List<Map<String, Object>> fullYearDates = ReaderTxtFileUtil.readerTxt(oaLegalHolidayUrl);
        return fullYearDates.stream()
                .map(f -> MapUtil.getStr(f, "date"))
                .filter(dataStr::contains).collect(Collectors.toList());
    }

    @Override
    public Boolean savePmsAttendanceRecord(ProjectRecordCreateReqVO createReqVO) {
        log.info("建能通发起打卡，params:{}", JsonUtils.toJsonString(createReqVO));
        AttendanceRecordDTO recordDTO = new AttendanceRecordDTO();
        recordDTO.setUserId(createReqVO.getUserId().intValue());
        recordDTO.setProjectId(createReqVO.getProjectId().intValue());
        Date checkInTime = TimeUtils.parseAsDateTime(TimeUtils.formatAsDateTime(createReqVO.getCheckInTime()));
        ExceptionThrowUtils.throwIfNull(checkInTime, GlobalErrorCodeConstants.BAD_REQUEST);
        recordDTO.setClockDate(TimeUtils.format(checkInTime, "yyyy-MM-dd"));
        recordDTO.setClockTime(TimeUtils.format(checkInTime, "HH:mm:ss"));
        recordDTO.setClockPosition(createReqVO.getRemarkIn());
        recordDTO.setClockLongitude(createReqVO.getLongitude());
        recordDTO.setClockLatitude(createReqVO.getLatitude());
        recordDTO.setPhotoUrl(createReqVO.getPhotoUrl());
        recordDTO.setOnline(createReqVO.isOnline());
        int userId = recordDTO.getUserId() > 0 ? recordDTO.getUserId() : loginUserId();
        this.saveAttendanceRecord(recordDTO, userId);
        return true;
    }

    @Override
    public PageResult<ProjectRecordDO> queryPmsAttendanceRecordPage(ProjectRecordPageReqVO pageVo) {
        log.info("建能通查询考勤记录，params:{}", JsonUtils.toJsonString(pageVo));
        if (Objects.nonNull(pageVo.getPunch()) && (Objects.isNull(pageVo.getStartTime()) || Objects.isNull(pageVo.getEndTime()))) {
            Date punchDate = TimeTransUtils.transSecondTimestamp2Date(pageVo.getPunch());
            Date startDateTime = TimeUtils.getDayStart(punchDate);
            Date endDateTime = TimeUtils.getDayEnd(punchDate);
            log.info("建能通查询考勤记录-通过punch重置开始和结束时间，punch:{}, startTime:{}，endTime:{}",
                    TimeUtils.formatAsDateTime(punchDate), TimeUtils.formatAsDateTime(startDateTime), TimeUtils.formatAsDateTime(endDateTime));
            pageVo.setStartTime(TimeTransUtils.transMilliTimestamp2SecondTimestamp(startDateTime.getTime()));
            pageVo.setEndTime(TimeTransUtils.transMilliTimestamp2SecondTimestamp(endDateTime.getTime()));
        }
        int startTime = Objects.isNull(pageVo.getStartTime()) ? 0 : pageVo.getStartTime().intValue();
        int endTime = Objects.isNull(pageVo.getEndTime()) ? 0 : pageVo.getEndTime().intValue();
        Integer pageNo = pageVo.getPageNo();
        Integer pageSize = pageVo.getPageSize();

        Pageable pageable = pageNo != null ? PageRequest.of(pageNo - 1, pageSize, Sort.unsorted()) : null;
        List<SysAttendanceRecordEntity> list = this.getDefaultAttendanceRecordList(0L, pageVo.getProjectId(),
                pageVo.getUserId(), startTime, endTime, pageable);
        if (list.isEmpty()) {
            return PageResult.empty();
        }

        Map<Long, AdminUserDO> userMap = userService.getUserMap(list
                .stream()
                .map(entity -> (long) entity.getUserId())
                .collect(Collectors.toSet()));
        Map<Long, ProjectDO> projectMap = projectService.getProjectMap(list
                .stream()
                .map(entity -> (long) entity.getProjectId())
                .collect(Collectors.toSet()));

        List<ProjectRecordDO> result = list.stream().map(entity -> {
            ProjectRecordDO recordDO = new ProjectRecordDO();
            recordDO.setId((long) entity.getId());
            recordDO.setUserId((long) entity.getUserId());
            recordDO.setCheckInTime(TimeUtils.parseAsLocalDateTime(entity.getClockInTime()));
            recordDO.setCheckOutTime(TimeUtils.parseAsLocalDateTime(entity.getClockOffTime()));
            recordDO.setCheckInStatus((byte) (Objects.equals(entity.getClockInStatus(), AttendanceClockStatusEnum.NORMAL_CLOCK) ? 0 : 1));
            recordDO.setCheckOutStatus((byte) (Objects.equals(entity.getClockOffStatus(), AttendanceClockStatusEnum.NORMAL_CLOCK) ? 0 : 1));
            recordDO.setPunchDate(TimeUtils.parseAsDate(entity.getAttendanceDateStr(), "yyyy-MM-dd"));
            recordDO.setAttStatus((byte) 1);
            recordDO.setProjectId(entity.getProjectId().longValue());
            recordDO.setRemarkIn(entity.getClockInPosition());
            recordDO.setRemarkOut(entity.getClockOffPosition());
            AdminUserDO user = userMap.get((long) entity.getUserId());
            if (Objects.nonNull(user)) {
                recordDO.setUserName(user.getNickname());
            }
            ProjectDO project = projectMap.get(Long.valueOf(entity.getProjectId()));
            if (Objects.nonNull(project)) {
                recordDO.setProjectName(project.getName());
            }
            recordDO.setPhotoUrl(entity.getClockInPhotoUrl());
            recordDO.setCreateTime(TimeUtils.parseAsLocalDateTimeByTimestamp((long) entity.getCreatedTime()));
            recordDO.setUpdateTime(TimeUtils.parseAsLocalDateTimeByTimestamp((long) entity.getModifiedTime()));
            recordDO.setCreator(entity.getCreatedBy() + "");
            recordDO.setUpdater(entity.getModifiedBy() + "");
            recordDO.setDeleted(false);
            return recordDO;
        }).collect(Collectors.toList());
        int count = this.getDefaultAttendanceRecordCount(0L, pageVo.getProjectId(), pageVo.getUserId(), startTime, endTime);
        return new PageResult<>(result, (long) count);
    }

    @Override
    public Map<Integer, Map<Integer, List<Long>>> getRecentlyMonthAttendanceDateList(SysAttendanceRecordQueryParam param) {
        if (CollectionUtils.isEmpty(param.getUserIds()) || Objects.isNull(param.getQueryAttendanceMonth())) {
            return Collections.emptyMap();
        }
        // 获取查询时间区间，默认查询近两个月的数据，避免查询到的无用数据太多
        Integer queryMonthCount = param.getQueryMonthCount();
        Date queryMonthDate = TimeTransUtils.transSecondTimestamp2Date(param.getQueryAttendanceMonth());
        Date previousMonth = TimeUtils.parseAsDate(
                TimeUtils.plusMonths(TimeUtils.format(queryMonthDate, "yyyyMM"), -queryMonthCount),
                "yyyyMM");
        if (Objects.isNull(previousMonth)) {
            return Collections.emptyMap();
        }
        long endTimestamp = TimeTransUtils.transMilliTimestamp2SecondTimestamp(
                TimeUtils.getDayEnd(TimeUtils.getMonthEndDate(queryMonthDate)).getTime());
        long startTimestamp = TimeTransUtils.transMilliTimestamp2SecondTimestamp(
                TimeUtils.getDayStart(TimeUtils.getMonthStartDate(previousMonth)).getTime());
        log.info("获取指定用户的近几个月考勤记录，userIds:{}, startTime:{}, endTime:{}", param.getUserIds(), startTimestamp, endTimestamp);
        List<SysAttendanceRecordEntity> attendanceListByParams = mapper.getAttendanceRecordListByParams(
                "(" + Joiner.on(",").join(param.getUserIds()) + ")", (int) startTimestamp, (int) endTimestamp);
        Map<Integer, Map<Integer, List<Long>>> result = new HashMap<>(16);
        attendanceListByParams
                .stream()
                .collect(Collectors.groupingBy(SysAttendanceRecordEntity::getUserId))
                .forEach((userId, entityList) -> {
                    Map<Integer, List<Long>> maxRecordDateMap = new HashMap<>(16);
                    entityList.stream()
                            .collect(Collectors.groupingBy(SysAttendanceRecordEntity::getProjectId))
                            .forEach((projectId, entities) -> {
                                List<Long> attendanceDateList = entities
                                        .stream()
                                        .filter(entity -> Objects.nonNull(entity.getClockInTime()))
                                        .map(entity -> TimeUtils.parseAsDateTime(entity.getClockInTime()).getTime())
                                        .sorted()
                                        .collect(Collectors.toList());
                                maxRecordDateMap.put(projectId, attendanceDateList);
                            });
                    result.put(userId, maxRecordDateMap);
                });
        return result;
    }

}
