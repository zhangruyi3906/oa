package com.lh.oa.module.system.service.monthstatistics;

import cn.hutool.core.collection.CollUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import com.lh.oa.module.system.controller.admin.monthstatistics.vo.*;
import com.lh.oa.module.system.convert.monthstatistics.MonthStatisticsConvert;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.dataobject.monthstatistics.MonthStatisticsDO;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;
import com.lh.oa.module.system.dal.mysql.attendanceRule.AttendanceRuleMapper;
import com.lh.oa.module.system.dal.mysql.holidayInfo.HolidayInfoMapper;
import com.lh.oa.module.system.dal.mysql.monthstatistics.MonthStatisticsMapper;
import com.lh.oa.module.system.dal.mysql.projectAttendanceRule.ProjectAttendanceRuleMapper;
import com.lh.oa.module.system.dal.mysql.projectrecord.ProjectRecordMapper;
import com.lh.oa.module.system.dal.mysql.record.RecordMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.dal.mysql.userProject.UserProjectMapper;
import com.lh.oa.module.system.util.roleScope.RoleScopeUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.lh.oa.module.system.enums.ErrorCodeConstants.*;

/**
 * 考勤月统计 Service 实现类
 *
 * @author
 */
@Service
@Validated
public class MonthStatisticsServiceImpl implements MonthStatisticsService {

    @Resource
    private MonthStatisticsMapper monthStatisticsMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private AttendanceRuleMapper ruleMapper;

    @Resource
    private HolidayInfoMapper holidayInfoMapper;

    @Resource
    private UserProjectMapper userProjectMapper;
    @Resource
    private ProjectAttendanceRuleMapper projectAttendanceRuleMapper;
    @Resource
    private ProjectRecordMapper projectRecordMapper;

    @Resource
    private RoleScopeUtils utils;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


    @Override
    public Long createMonthStatistics(RecordMonthVO createReqVO) {
        String attendanceMonth = createReqVO.getAttendanceMonth();
        int year = Integer.parseInt(attendanceMonth.substring(0, 4));
        int month = Integer.parseInt(attendanceMonth.substring(5, 7));
        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfMonth = localDate.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth2 = YearMonth.of(now.getYear(), now.getMonth());
        LocalDateTime endOfMonth;
        if(yearMonth.equals(yearMonth2)){
             endOfMonth = now.minusDays(1).toLocalDate().atTime(23, 59, 59);
        }else {
            endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        }
        if(startOfMonth.isAfter(endOfMonth)){
            throw exception(START_AFTER_END);
        }
        ArrayList<MonthStatisticsCreateReqVO> listMonth = new ArrayList<>();
        if (CollUtil.isNotEmpty(createReqVO.getDeptIds())) {
            List<Long> collect = adminUserMapper.selectList(new LambdaQueryWrapperX<AdminUserDO>().inIfPresent(AdminUserDO::getDeptId, createReqVO.getDeptIds())).stream().map(AdminUserDO::getId).collect(Collectors.toList());
            createReqVO.setUserIds(collect);
        }
        if (CollUtil.isNotEmpty(createReqVO.getUserIds())) {
            createReqVO.getUserIds().forEach(s -> {
                List<MonthStatisticsDO> dos = monthStatisticsMapper.selectList(new LambdaQueryWrapperX<MonthStatisticsDO>()
                        .eq(MonthStatisticsDO::getUserId, s)
                        .eq(MonthStatisticsDO::getAttendanceMonth,createReqVO.getAttendanceMonth())
                        .eq(MonthStatisticsDO::getAttStatus, 0)); // 部门
                if(CollUtil.isNotEmpty(dos)){
                    return;
                }
                Long deptId = adminUserMapper.selectById(s).getDeptId();
                List<AttendanceRuleDO> list = ruleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>().eq(AttendanceRuleDO::getDeptId, deptId));
                if(CollUtil.isEmpty(list)){
                    return;
                }
                AttendanceRuleDO attendanceRuleDO = list.get(0);
                Integer syncHolidays = attendanceRuleDO.getSyncHolidays();
                if (syncHolidays == 1) {
                    String[] split = attendanceRuleDO.getWorkDays().split(",");
                    List<Integer> intArray = new ArrayList<>();
                    for (String value : split) {
                        intArray.add(Integer.parseInt(value));
                    }
                    int start = startOfMonth.getYear() * 10000 + startOfMonth.getMonthValue() * 100 + startOfMonth.getDayOfMonth();
                    int end = endOfMonth.getYear() * 10000 + endOfMonth.getMonthValue() * 100 + endOfMonth.getDayOfMonth();
                    List<HolidayInfoDO> holidayInfoDOS = holidayInfoMapper.selectList(new LambdaQueryWrapperX<HolidayInfoDO>()
                            .betweenIfPresent(HolidayInfoDO::getDate, start, end).inIfPresent(HolidayInfoDO::getWeek, intArray));
                    List<Date> dates = new ArrayList<>();
                    for (HolidayInfoDO infoDO : holidayInfoDOS) {
                        try {
                            Date parse = dateFormat.parse(String.valueOf(infoDO.getDate()));
                            dates.add(parse);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Map<Date, RecordDO> attMap = new HashMap<>();
                    List<RecordDO> recordDOS;
                    if (CollUtil.isNotEmpty(dates)) {
                        recordDOS = recordMapper.selectList(new LambdaQueryWrapperX<RecordDO>().eq(RecordDO::getUserId, s).in(RecordDO::getPunchDate, dates));
                        if (CollUtil.isEmpty(recordDOS)) {
                            AdminUserDO adminUserDO = adminUserMapper.selectById(s);
                            recordDOS.add(new RecordDO().setAttStatus((byte) 1).setPunchDate(dates.get(0)).setUserId(adminUserDO.getId()).setUserName(adminUserDO.getNickname()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(adminUserDO.getDeptId()));
                        }
                        Map<Date, RecordDO> collect = recordDOS.stream().collect(Collectors.toMap(RecordDO::getPunchDate, recordDO -> recordDO));

                        List<RecordDO> recordList = new ArrayList<>(collect.values());
                        RecordDO recordDO = recordList.get(0);
                        for (Date punch : dates) {
                            if (!collect.containsKey(punch)) {
                                collect.put(punch, new RecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(attendanceRuleDO.getDeptId()));
                                attMap.put(punch, new RecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(attendanceRuleDO.getDeptId()));
                            }
                        }
                        recordMapper.insertBatch(attMap.values());
                        long count = recordList.stream().filter(re -> {
                            Byte checkInStatus = re.getCheckInStatus();
                            Byte checkOutStatus = re.getCheckOutStatus();
                            return Optional.ofNullable(checkInStatus).orElse((byte) 3) == 1 || Optional.ofNullable(checkOutStatus).orElse((byte) 3) == 1;
                        }).count();
                        long count1 = recordList.stream().map(m -> m.getAttStatus() == (byte)1).count();
                        int size = recordList.size();
                        MonthStatisticsCreateReqVO monthVO = new MonthStatisticsCreateReqVO();
                        monthVO.setUserId(s);
                        monthVO.setActualWorkingDays(size);
//                        monthVO.setActualWorkingDays(size-(int) count1);
                        monthVO.setAttendanceMonth(attendanceMonth);
                        monthVO.setTotalAbsenceDays((int) count);
                        monthVO.setTotalWorkingDays(totalDays);
                        monthVO.setTotalLeaveDays(attMap.values().size());
//                        monthVO.setTotalLeaveDays(attMap.values().size()+(int) count1);
                        monthVO.setUserName(recordDO.getUserName());
                        monthVO.setDeptName(attendanceRuleDO.getDeptName());
                        monthVO.setDeptId(attendanceRuleDO.getDeptId());
                        monthVO.setAttStatus(0);
                        listMonth.add(monthVO);
                    }
                } else {
                    int start = startOfMonth.getYear() * 10000 + startOfMonth.getMonthValue() * 100 + startOfMonth.getDayOfMonth();
                    int end = endOfMonth.getYear() * 10000 + endOfMonth.getMonthValue() * 100 + endOfMonth.getDayOfMonth();
                    List<HolidayInfoDO> holidayInfoDOS = holidayInfoMapper.selectList(new LambdaQueryWrapperX<HolidayInfoDO>().betweenIfPresent(HolidayInfoDO::getDate, start, end).eqIfPresent(HolidayInfoDO::getWorkday, 1));
                    List<Date> dates = new ArrayList<>();
                    for (HolidayInfoDO infoDO : holidayInfoDOS) {
                        try {
                            Date parse = dateFormat.parse(String.valueOf(infoDO.getDate()));
                            dates.add(parse);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Map<Date, RecordDO> attMap = new HashMap<>();
                    List<RecordDO> recordDOS;
                    if (CollUtil.isNotEmpty(dates)) {
                        recordDOS = recordMapper.selectList(new LambdaQueryWrapperX<RecordDO>().eq(RecordDO::getUserId, s).inIfPresent(RecordDO::getPunchDate, dates));
                        if (CollUtil.isEmpty(recordDOS)) {
                            AdminUserDO adminUserDO = adminUserMapper.selectById(s);
                            recordDOS.add(new RecordDO().setAttStatus((byte) 0).setPunchDate(dates.get(0)).setUserId(adminUserDO.getId()).setUserName(adminUserDO.getNickname()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(adminUserDO.getDeptId()));
                        }
                        Map<Date, RecordDO> collect = recordDOS.stream().collect(Collectors.toMap(RecordDO::getPunchDate, recordDO -> recordDO));
                        List<RecordDO> recordList = new ArrayList<>(collect.values());
                        RecordDO recordDO = recordList.get(0);
                        for (Date punch : dates) {
                            if (!collect.containsKey(punch)) {
                                collect.put(punch, new RecordDO().setAttStatus((byte) 0).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(attendanceRuleDO.getDeptId()));
                                attMap.put(punch, new RecordDO().setAttStatus((byte) 0).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setDeptName(attendanceRuleDO.getDeptName()).setDeptId(attendanceRuleDO.getDeptId()));
                            }
                        }
                        recordMapper.insertBatch(attMap.values());
                        long count = recordList.stream().filter(re -> {
                            Byte checkInStatus = re.getCheckInStatus();
                            Byte checkOutStatus = re.getCheckOutStatus();
                            return Optional.ofNullable(checkInStatus).orElse((byte) 3) == 1 || Optional.ofNullable(checkOutStatus).orElse((byte) 3) == 1;
                        }).count();
                        long count1 = recordList.stream().map(m -> m.getAttStatus() == (byte)1).count();
                        int size = recordList.size();
                        MonthStatisticsCreateReqVO monthVO = new MonthStatisticsCreateReqVO();
                        monthVO.setUserId(s);
                        monthVO.setActualWorkingDays(size);
//                        monthVO.setActualWorkingDays(size-(int) count1);
                        monthVO.setAttendanceMonth(attendanceMonth);
                        monthVO.setTotalAbsenceDays((int) count);
                        monthVO.setTotalWorkingDays(totalDays);
                        monthVO.setTotalLeaveDays(attMap.values().size());
//                        monthVO.setTotalLeaveDays(attMap.values().size()+(int) count1);
                        monthVO.setUserName(recordDO.getUserName());
                        monthVO.setDeptName(attendanceRuleDO.getDeptName());
                        monthVO.setDeptId(attendanceRuleDO.getDeptId());
                        monthVO.setAttStatus(0);
                        listMonth.add(monthVO);
                    }
                }
            });
            List<MonthStatisticsDO> monthStatisticsDOS = MonthStatisticsConvert.INSTANCE.convertList03(listMonth);
            monthStatisticsMapper.insertBatch(monthStatisticsDOS);
            return (long) monthStatisticsDOS.size();
        }
        // 返回
        return (long) 1;
    }

    @Override
    public Long createMonthProjectStatistics(RecordMonthVO createReqVO) {
        String attendanceMonth = createReqVO.getAttendanceMonth();
        int year = Integer.parseInt(attendanceMonth.substring(0, 4));
        int month = Integer.parseInt(attendanceMonth.substring(5, 7));
        YearMonth yearMonth = YearMonth.of(year, month);
        int totalDays = yearMonth.lengthOfMonth();
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfMonth = localDate.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth2 = YearMonth.of(now.getYear(), now.getMonth());
        LocalDateTime endOfMonth;
        if(yearMonth.equals(yearMonth2)){
            endOfMonth = now.minusDays(1).toLocalDate().atTime(23, 59, 59);// TODO [Rz Liu]: 2023-08-14 应该是当天
        }else {
            endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        }
        if(startOfMonth.isAfter(endOfMonth)){
            throw exception(START_AFTER_END);
        }
        List<MonthStatisticsCreateReqVO> listMonth = new ArrayList<>();

        if (CollUtil.isNotEmpty(createReqVO.getProjectId())) {
            List<Long> collect = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().inIfPresent(UserProjectDO::getProjectId, createReqVO.getProjectId()).eq(UserProjectDO::getIsRecord, 1)).stream().map(UserProjectDO::getUserId).distinct().collect(Collectors.toList());
            createReqVO.setUserIds(collect);
        }
        if (CollUtil.isNotEmpty(createReqVO.getUserIds())) {
            for (Long s : createReqVO.getUserIds()) {
                List<MonthStatisticsDO> dos = monthStatisticsMapper.selectList(new LambdaQueryWrapperX<MonthStatisticsDO>()
                        .eq(MonthStatisticsDO::getUserId, s)
                        .eq(MonthStatisticsDO::getAttendanceMonth, createReqVO.getAttendanceMonth())
                        .eq(MonthStatisticsDO::getAttStatus, 1)); //项目
                if (CollUtil.isNotEmpty(dos)) {
                    continue;
                }
                List<Long> proList = userProjectMapper.selectList(new LambdaQueryWrapperX<UserProjectDO>().eq(UserProjectDO::getUserId, s).eq(UserProjectDO::getStatus, 1)).stream().map(UserProjectDO::getProjectId).collect(Collectors.toList());
                for (Long pro : proList) {
                    List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectList(new LambdaQueryWrapperX<ProjectAttendanceRuleDO>().eq(ProjectAttendanceRuleDO::getProjectId, pro));
                    if (CollUtil.isEmpty(ruleDOS)) {
                        break;
                    }
                    ProjectAttendanceRuleDO projectAttendanceRuleDO = ruleDOS.get(0);
                    Integer syncHolidays = projectAttendanceRuleDO.getSyncHolidays();
                    if (syncHolidays == 1) {
                        String[] split = projectAttendanceRuleDO.getWorkDays().split(",");
                        List<Integer> intArray = new ArrayList<>();
                        for (String value : split) {
                            intArray.add(Integer.parseInt(value));
                        }
                        int start = startOfMonth.getYear() * 10000 + startOfMonth.getMonthValue() * 100 + startOfMonth.getDayOfMonth();
                        int end = endOfMonth.getYear() * 10000 + endOfMonth.getMonthValue() * 100 + endOfMonth.getDayOfMonth();
                        List<HolidayInfoDO> holidayInfoDOS = holidayInfoMapper.selectList(new LambdaQueryWrapperX<HolidayInfoDO>()
                                .betweenIfPresent(HolidayInfoDO::getDate, start, end).inIfPresent(HolidayInfoDO::getWeek, intArray));
                        List<Date> dates = new ArrayList<>();
                        for (HolidayInfoDO infoDO : holidayInfoDOS) {
                            try {
                                Date parse = dateFormat.parse(String.valueOf(infoDO.getDate()));
                                dates.add(parse);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Map<Date, ProjectRecordDO> attMap = new HashMap<>();
                        List<ProjectRecordDO> recordDOS;
                        recordDOS = projectRecordMapper.selectList(new LambdaQueryWrapperX<ProjectRecordDO>().eq(ProjectRecordDO::getUserId, s).in(ProjectRecordDO::getPunchDate, dates));
                        if (CollUtil.isEmpty(recordDOS)) {
                            AdminUserDO adminUserDO = adminUserMapper.selectById(s);
                            recordDOS.add(new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(dates.get(0)).setUserId(adminUserDO.getId()).setUserName(adminUserDO.getNickname()).setProjectName(projectAttendanceRuleDO.getProjectName()).setProjectId(projectAttendanceRuleDO.getProjectId()));
                        }
                        Map<Date, ProjectRecordDO> collect = recordDOS.stream().collect(Collectors.toMap(ProjectRecordDO::getPunchDate, recordDO -> recordDO));
                        List<ProjectRecordDO> recordList = new ArrayList<>(collect.values());
                        ProjectRecordDO recordDO = recordList.get(0);
                        for (Date punch : dates) {
                            if (!collect.containsKey(punch)) {
                                collect.put(punch, new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setProjectName(recordDO.getProjectName()).setProjectId(recordDO.getProjectId()));
                                attMap.put(punch, new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setProjectName(recordDO.getProjectName()).setProjectId(recordDO.getProjectId()));
                            }
                        }
                        projectRecordMapper.insertBatch(attMap.values());
                        long count = recordList.stream().filter(re -> {
                                    Byte checkInStatus = re.getCheckInStatus();
                                    Byte checkOutStatus = re.getCheckOutStatus();
                                    return Optional.ofNullable(checkInStatus).orElse((byte) 3) == 1 || Optional.ofNullable(checkOutStatus).orElse((byte) 3) == 1;
                                }).count();
                        long count1 = recordList.stream().map(m -> m.getAttStatus() == (byte) 1).count();
                        int size = recordList.size();
                        MonthStatisticsCreateReqVO monthVO = new MonthStatisticsCreateReqVO();
                        monthVO.setUserId(s);
                        monthVO.setActualWorkingDays(size);
//                        monthVO.setActualWorkingDays(size - (int) count1);
                        monthVO.setAttendanceMonth(attendanceMonth);
                        monthVO.setTotalAbsenceDays((int) count);
                        monthVO.setTotalWorkingDays(totalDays);
                        monthVO.setTotalLeaveDays(attMap.values().size());
//                        monthVO.setTotalLeaveDays(attMap.values().size() + (int) count1);
                        monthVO.setUserName(recordDO.getUserName());
                        monthVO.setProjectName(projectAttendanceRuleDO.getProjectName());
                        monthVO.setProjectId(projectAttendanceRuleDO.getProjectId());
                        monthVO.setAttStatus(1);
                        listMonth.add(monthVO);
                    } else {
                        int start = startOfMonth.getYear() * 10000 + startOfMonth.getMonthValue() * 100 + startOfMonth.getDayOfMonth();
                        int end = endOfMonth.getYear() * 10000 + endOfMonth.getMonthValue() * 100 + endOfMonth.getDayOfMonth();
                        List<HolidayInfoDO> holidayInfoDOS = holidayInfoMapper.selectList(new LambdaQueryWrapperX<HolidayInfoDO>().betweenIfPresent(HolidayInfoDO::getDate, start, end).eqIfPresent(HolidayInfoDO::getWorkday, 1));
                        List<Date> dates = new ArrayList<>();
                        for (HolidayInfoDO infoDO : holidayInfoDOS) {
                            try {
                                Date parse = dateFormat.parse(String.valueOf(infoDO.getDate()));
                                dates.add(parse);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Map<Date, ProjectRecordDO> attMap = new HashMap<>();
                        List<ProjectRecordDO> recordDOS;
                        recordDOS = projectRecordMapper.selectList(new LambdaQueryWrapperX<ProjectRecordDO>().eq(ProjectRecordDO::getUserId, s).in(ProjectRecordDO::getPunchDate, dates));
                        if (CollUtil.isEmpty(recordDOS)) {
                            AdminUserDO adminUserDO = adminUserMapper.selectById(s);
                            recordDOS.add(new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(dates.get(0)).setUserId(adminUserDO.getId()).setUserName(adminUserDO.getNickname()).setProjectName(projectAttendanceRuleDO.getProjectName()).setProjectId(projectAttendanceRuleDO.getProjectId()));
                        }
                        Map<Date, ProjectRecordDO> collect = recordDOS.stream().collect(Collectors.toMap(ProjectRecordDO::getPunchDate, recordDO -> recordDO));
                        List<ProjectRecordDO> recordList = new ArrayList<>(collect.values());
                        ProjectRecordDO recordDO = recordList.get(0);
                        for (Date punch : dates) {
                            if (!collect.containsKey(punch)) {
                                collect.put(punch, new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setProjectName(recordDO.getProjectName()).setProjectId(recordDO.getProjectId()));
                                attMap.put(punch, new ProjectRecordDO().setAttStatus((byte) 1).setPunchDate(punch).setUserId(recordDO.getUserId()).setUserName(recordDO.getUserName()).setProjectName(recordDO.getProjectName()).setProjectId(recordDO.getProjectId()));
                            }
                        }
                        projectRecordMapper.insertBatch(attMap.values());
                        long count = recordList.stream().filter(re -> {
                                    Byte checkInStatus = re.getCheckInStatus();
                                    Byte checkOutStatus = re.getCheckOutStatus();
                                    return Optional.ofNullable(checkInStatus).orElse((byte) 3) == 1 || Optional.ofNullable(checkOutStatus).orElse((byte) 3) == 1;
                                })
                                .count();
                        long count1 = recordList.stream().map(m -> m.getAttStatus() == (byte) 1).count();
                        int size = recordList.size();
                        MonthStatisticsCreateReqVO monthVO = new MonthStatisticsCreateReqVO();
                        monthVO.setUserId(s);
                        monthVO.setActualWorkingDays(size);
//                        monthVO.setActualWorkingDays(size - (int) count1);
                        monthVO.setAttendanceMonth(attendanceMonth);
                        monthVO.setTotalAbsenceDays((int) count);
                        monthVO.setTotalWorkingDays(totalDays);
                        monthVO.setTotalLeaveDays(attMap.values().size());
//                        monthVO.setTotalLeaveDays(attMap.values().size() + (int) count1);
                        monthVO.setUserName(recordDO.getUserName());
                        monthVO.setProjectName(projectAttendanceRuleDO.getProjectName());
                        monthVO.setProjectId(projectAttendanceRuleDO.getProjectId());
                        monthVO.setAttStatus(1);
                        listMonth.add(monthVO);
                    }
                }
            }
            List<MonthStatisticsDO> monthStatisticsDOS = MonthStatisticsConvert.INSTANCE.convertList03(listMonth);
        monthStatisticsMapper.insertBatch(monthStatisticsDOS);
        return (long) monthStatisticsDOS.size();
    }
    // 返回
        return(long)1;
}

    @Override
    public void updateMonthStatistics(MonthStatisticsUpdateReqVO updateReqVO) {
        // 校验存在
        validateMonthStatisticsExists(updateReqVO.getId());
        // 更新
        MonthStatisticsDO updateObj = MonthStatisticsConvert.INSTANCE.convert(updateReqVO);
        monthStatisticsMapper.updateById(updateObj);
    }

    @Override
    public void deleteMonthStatistics(Long id) {
        // 校验存在
        validateMonthStatisticsExists(id);
        // 删除
        monthStatisticsMapper.updateToDelete(id);
    }

    private void validateMonthStatisticsExists(Long id) {
        if (monthStatisticsMapper.selectById(id) == null) {
            throw exception(MONTH_STATISTICS_NOT_EXISTS);
        }
    }

    @Override
    public MonthStatisticsDO getMonthStatistics(Long id) {
        return monthStatisticsMapper.selectById(id);
    }

    @Override
    public List<MonthStatisticsDO> getMonthStatisticsList(Collection<Long> ids) {
        return monthStatisticsMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<MonthStatisticsDO> getMonthStatisticsPage(MonthStatisticsPageReqVO pageReqVO) {
        Set<Long> page = new HashSet<>(utils.isPage(getLoginUserId()));
        if (pageReqVO.getDeptId() != null) {
            if (page.contains(pageReqVO.getDeptId())) {
                page = new HashSet<>(Collections.singleton(pageReqVO.getDeptId()));
            } else {
                throw exception(WORK_LOG_is_ROLE);
            }
        }
        pageReqVO.setAttStatus(0);
        return monthStatisticsMapper.selectPage(page,pageReqVO);
    }

    @Override
    public PageResult<MonthStatisticsDO> getProjectMonthStatisticsPage(MonthStatisticsPageReqVO pageReqVO) {
        pageReqVO.setAttStatus(1);
        return monthStatisticsMapper.selectPage1(pageReqVO);
    }

    @Override
    public List<MonthStatisticsDO> getMonthStatisticsList(MonthStatisticsExportReqVO exportReqVO) {
        return monthStatisticsMapper.selectList(exportReqVO);
    }

}
