package com.lh.oa.module.system.service.projectrecord;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.ProjectAttendanceRuleListBaseVO;
import com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo.RuleVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.*;
import com.lh.oa.module.system.controller.admin.record.vo.CheckStatusProject;
import com.lh.oa.module.system.convert.projectrecord.ProjectRecordConvert;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.dataobject.projectAttendanceRule.ProjectAttendanceRuleDO;
import com.lh.oa.module.system.dal.dataobject.projectrecord.ProjectRecordDO;
import com.lh.oa.module.system.dal.mysql.holidayInfo.HolidayInfoMapper;
import com.lh.oa.module.system.dal.mysql.projectAttendanceRule.ProjectAttendanceRuleMapper;
import com.lh.oa.module.system.dal.mysql.projectrecord.ProjectRecordMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordCreateReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordPageReqVO;
import com.lh.oa.module.system.controller.admin.projectrecord.vo.ProjectRecordUpdateReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.lh.oa.module.system.enums.ErrorCodeConstants.*;

@Slf4j
@Service
public class ProjectRecordServiceImpl implements ProjectRecordService {
    @Resource
    private ProjectRecordMapper projectRecordMapper;
    @Resource
    private HolidayInfoMapper mapper;
    @Resource
    private ProjectAttendanceRuleMapper projectAttendanceRuleMapper;

    @Override
    public Boolean createRecordProject(@Valid ProjectRecordCreateReqVO createReqVO) {
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectByProjectList(createReqVO.getProjectId());
        if (ruleDOS.isEmpty()) {
            throw exception(PROJECT_ATTENDANCE_RULE_NOT_EXISTS);
        }
        ProjectAttendanceRuleListBaseVO exchange = exchange(ruleDOS);
        isProjectAttendance(createReqVO, exchange);
        return true;
    }

    @Override
    public Boolean updateRecordProject(ProjectRecordUpdateReqVO updateReqVO) {
        validateRecordExists(updateReqVO.getId());
        List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectByProjectList(updateReqVO.getProjectId());
        if (ruleDOS.isEmpty()) {
            throw exception(PROJECT_ATTENDANCE_RULE_NOT_EXISTS);
        }
        ProjectAttendanceRuleListBaseVO exchange = exchange(ruleDOS);
        updateHandleFixedPunchAttendance(updateReqVO, exchange, updateReqVO.getCheckOutTime());
        return true;
    }

    @Override
    public PageResult<ProjectRecordDO> getRecordProjectPage(ProjectRecordPageReqVO pageVO) {
        List<LocalDate> list = new ArrayList<>();
        if (pageVO.getPunch() != null) {
            Instant instant = Instant.ofEpochSecond(pageVO.getPunch());
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            pageVO.setPunchDate(localDate);
            LocalDate localDate1 = localDate.plusDays(1);
            list.add(localDate);
            list.add(localDate1);
        }
        PageResult<ProjectRecordDO> projectRecordDOPageResult = projectRecordMapper.selectPage(list, pageVO);
        // TODO [Rz Liu]: 2023-08-16 这一坨有问题，先注释掉，多次打卡在打卡数据入口时已经处理了
//        if (CollUtil.isNotEmpty(projectRecordDOPageResult.getList())) {
//            if (projectRecordDOPageResult.getList().size() == 1) {
//                List<ProjectAttendanceRuleDO> ruleDOS = projectAttendanceRuleMapper.selectByProjectList(projectRecordDOPageResult.getList().get(0).getProjectId());
//                if (ruleDOS.isEmpty()) {
//                    throw exception(PROJECT_ATTENDANCE_RULE_NOT_EXISTS);
//                } else {
//                    if (ruleDOS.get(0).getEndNextDay() != null) {
//                        LocalDateTime now = LocalDateTime.now();
//                        boolean equals = projectRecordDOPageResult.getList().get(0).getPunchDate().equals(now.toLocalDate());
//                        if (equals && now.toLocalTime().isAfter(ruleDOS.get(0).getEndNextDay())) {
//                            projectRecordDOPageResult.getList().remove(0);
//                        }
//                    } else {
//                        projectRecordDOPageResult.getList().remove(0);
//                    }
//                }
//            } else if (projectRecordDOPageResult.getList().size() == 2) {
//                projectRecordDOPageResult.getList().remove(1);
//            }
//        }
        return projectRecordDOPageResult;
    }

    private void validateRecordExists(Long id) {
        if (projectRecordMapper.selectById(id) == null) {
            throw exception(RECORD_NOT_EXISTS);
        }
    }

    private double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        final int EARTH_RADIUS_KM = 6371;
        double latRad1 = Math.toRadians(latitude1);
        double lonRad1 = Math.toRadians(longitude1);
        double latRad2 = Math.toRadians(latitude2);
        double lonRad2 = Math.toRadians(longitude2);
        double diffLat = latRad2 - latRad1;
        double diffLon = lonRad2 - lonRad1;
        double a = Math.pow(Math.sin(diffLat / 2), 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(Math.sin(diffLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        return distance * 1000;
    }

    private ProjectAttendanceRuleListBaseVO exchange(List<ProjectAttendanceRuleDO> list) {
        ProjectAttendanceRuleListBaseVO baseVO = new ProjectAttendanceRuleListBaseVO();
        ProjectAttendanceRuleDO projectAttendanceRuleDO = list.get(0);
        baseVO.setProjectId(projectAttendanceRuleDO.getProjectId());
        baseVO.setProjectName(projectAttendanceRuleDO.getProjectName());
        baseVO.setPunchType(projectAttendanceRuleDO.getPunchType());
        baseVO.setPunchTypeName(projectAttendanceRuleDO.getPunchTypeName());
        baseVO.setAllopatricStatus(projectAttendanceRuleDO.getAllopatricStatus());
        baseVO.setSyncHolidays(projectAttendanceRuleDO.getSyncHolidays());
        baseVO.setWorkDays(projectAttendanceRuleDO.getWorkDays());
        baseVO.setEndNextDay(projectAttendanceRuleDO.getEndNextDay());
        baseVO.setFlexibleCheckInStart(projectAttendanceRuleDO.getFlexibleCheckInStart());
        baseVO.setFlexibleCheckInEnd(projectAttendanceRuleDO.getFlexibleCheckInEnd());
        List<RuleVO> ruleVOList = list.stream()
                .map(s -> new RuleVO(s.getPunchName(), s.getPunchRadius(), s.getLatiLong()))
                .collect(Collectors.toList());
        baseVO.setList(ruleVOList);
        return baseVO;
    }

    private void isProjectAttendance(ProjectRecordCreateReqVO createReqVO, ProjectAttendanceRuleListBaseVO exchange) {
        LocalDateTime now = createReqVO.getCheckInTime();
        int value = now.getDayOfWeek().getValue();

        if (exchange.getPunchType() == 1) {
            exchange.setAllopatricStatus(0); // TODO [Rz Liu]: 2023-08-16 自由考勤允许异地打卡，下次让前端改
        }

        // 0 同步非国家节假日， 1 不同步非国家节假日
        if (exchange.getSyncHolidays() == 1) {
            String[] workDaysArray = exchange.getWorkDays().split(",");
            List<String> workDaysList = Arrays.asList(workDaysArray);
            boolean isWorkDay = workDaysList.contains(String.valueOf(value));

            if (isWorkDay) {
                if (exchange.getPunchType() == 1) {
                    handleFreePunchAttendance(createReqVO, exchange, now); // 1 自由打卡
                } else {
                    handleFixedPunchAttendance(createReqVO, exchange, now); // 0 固定打卡
                }
            } else {
                throw exception(HOLIDAY_NOT_RECORD);
            }
        } else {
            int formattedDate = now.getYear() * 10000 + now.getMonthValue() * 100 + now.getDayOfMonth();
            Integer workday = mapper.selectOne(new LambdaQueryWrapperX<HolidayInfoDO>().eq(HolidayInfoDO::getDate, formattedDate)).getWorkday();

            if (workday == 2) {
                throw exception(HOLIDAY_NOT_RECORD);
            } else {
                handleFixedPunchAttendance(createReqVO, exchange, now);
            }
        }
    }

    private void handleFreePunchAttendance(ProjectRecordCreateReqVO createReqVO, ProjectAttendanceRuleListBaseVO exchange, LocalDateTime now) {
        CheckStatusProject checkStatus = new CheckStatusProject();
        // 自由打卡不存在迟到早退的情况
        if (exchange.getAllopatricStatus() == 0) { // 0 允许异地打卡
            createReqVO.setCheckInStatus((byte) 0);
            createReqVO.setAttStatus((byte) 1);
            createReqVO.setProjectId(exchange.getProjectId());
            if (exchange.getEndNextDay() != null && now.toLocalTime().isAfter(exchange.getEndNextDay())) {
                createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate().plusDays(1));
            } else {
                createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate());
            }
//            createReqVO.setCheckOutTime(createReqVO.getCheckInTime());
//            createReqVO.setRemarkOut(createReqVO.getRemarkIn());
//            createReqVO.setCheckOutStatus((byte) 0);
            createReqVO.setProjectName(exchange.getProjectName());
            ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert(createReqVO);

            List<ProjectRecordDO> projectRecordDOS = projectRecordMapper.selectList(new LambdaUpdateWrapper<ProjectRecordDO>()
                    .eq(ProjectRecordDO::getPunchDate, createReqVO.getPunchDate()));
            if (projectRecordDOS != null && projectRecordDOS.size() > 0) {
                projectRecordMapper.update(null, new LambdaUpdateWrapper<ProjectRecordDO>()
                        .eq(ProjectRecordDO::getUserId, createReqVO.getUserId())
                        .eq(ProjectRecordDO::getProjectId, createReqVO.getProjectId())
                        .eq(ProjectRecordDO::getPunchDate,createReqVO.getPunchDate())
                        .set(ProjectRecordDO::getDeleted, 1));
            }
            projectRecordMapper.insert(records);
        } else {
            for (RuleVO rule : exchange.getList()) {
                String[] coordinates = rule.getLatiLong().split(",");
                double centerLatitude = Double.parseDouble(coordinates[0]);
                double centerLongitude = Double.parseDouble(coordinates[1]);
                double punchRadiusValue = Double.parseDouble(rule.getPunchRadius());
                double distance = calculateDistance(createReqVO.getLatitude(), createReqVO.getLongitude(), centerLatitude, centerLongitude);

                if (distance <= punchRadiusValue) {
                    checkStatus.setCheckStatus((byte) 0);
                    checkStatus.setCheckId(exchange.getProjectId());
                    checkStatus.setCheckName(exchange.getProjectName());
                    break;
                }
            }

            if (checkStatus.getCheckStatus() == null) {
                throw exception(ATTENDANCE_ROUND_NOT_EXISTS);
            } else {
                createReqVO.setCheckInStatus(checkStatus.getCheckStatus());
                createReqVO.setAttStatus((byte) 1);
                createReqVO.setProjectId(checkStatus.getCheckId());
                createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate());
//                createReqVO.setCheckOutTime(createReqVO.getCheckInTime());
//                createReqVO.setRemarkOut(createReqVO.getRemarkIn());
//                createReqVO.setCheckOutStatus(checkStatus.getCheckStatus());
                createReqVO.setProjectName(checkStatus.getCheckName());
                ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert(createReqVO);

                List<ProjectRecordDO> projectRecordDOS = projectRecordMapper.selectList(new LambdaUpdateWrapper<ProjectRecordDO>()
                        .eq(ProjectRecordDO::getPunchDate, createReqVO.getPunchDate()));
                if (projectRecordDOS != null && projectRecordDOS.size() > 0) {
                    projectRecordMapper.update(null, new LambdaUpdateWrapper<ProjectRecordDO>()
                            .eq(ProjectRecordDO::getUserId, createReqVO.getUserId())
                            .eq(ProjectRecordDO::getProjectId, createReqVO.getProjectId())
                            .eq(ProjectRecordDO::getPunchDate,createReqVO.getPunchDate())
                            .set(ProjectRecordDO::getDeleted, 1));
                }
                projectRecordMapper.insert(records);
            }
        }
    }

    private void handleFixedPunchAttendance(ProjectRecordCreateReqVO createReqVO, ProjectAttendanceRuleListBaseVO exchange, LocalDateTime now) {
        if (exchange.getAllopatricStatus() == 1) { // 1 不允许异地打卡
            CheckStatusProject checkStatus = new CheckStatusProject();
            List<RuleVO> ruleList = exchange.getList();
            boolean isWithinRange = false;
            for (RuleVO rule : ruleList) {
                String[] centerPoints = rule.getLatiLong().split("/");
                for (String centerPoint : centerPoints) {
                    String[] coordinates = centerPoint.split(",");
                    double centerLatitude = Double.parseDouble(coordinates[0]);
                    double centerLongitude = Double.parseDouble(coordinates[1]);
                    double distance = calculateDistance(createReqVO.getLatitude(), createReqVO.getLongitude(), centerLatitude, centerLongitude);
                    if (distance <= Double.parseDouble(rule.getPunchRadius())) {
                        isWithinRange = true;
                        break;
                    }
                }
            }
            if (isWithinRange) {
                LocalTime flexibleCheckInStart = exchange.getFlexibleCheckInStart();
                if (now.toLocalTime().isBefore(flexibleCheckInStart)) {
                    // 0：正常
                    checkStatus.setCheckStatus((byte) 0);
                } else {
                    // 1：迟到
                    checkStatus.setCheckStatus((byte) 1);
                }
            }
            if (checkStatus.getCheckStatus() == null) {
                throw exception(ATTENDANCE_ROUND_NOT_EXISTS);
            }
            createReqVO.setCheckInStatus(checkStatus.getCheckStatus());
            createReqVO.setAttStatus((byte) 1);
            createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate());
            ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert(createReqVO);

            List<ProjectRecordDO> projectRecordDOS = projectRecordMapper.selectList(new LambdaUpdateWrapper<ProjectRecordDO>()
                    .eq(ProjectRecordDO::getPunchDate, createReqVO.getPunchDate()));
            if (projectRecordDOS != null && projectRecordDOS.size() > 0) {
                projectRecordMapper.update(null, new LambdaUpdateWrapper<ProjectRecordDO>()
                        .eq(ProjectRecordDO::getUserId, createReqVO.getUserId())
                        .eq(ProjectRecordDO::getProjectId, createReqVO.getProjectId())
                        .eq(ProjectRecordDO::getPunchDate,createReqVO.getPunchDate())
                        .set(ProjectRecordDO::getDeleted, 1));
            }
            projectRecordMapper.insert(records);
        } else {
            CheckStatusProject checkStatus = new CheckStatusProject();
            LocalTime flexibleCheckInStart = exchange.getFlexibleCheckInStart();
            if (now.toLocalTime().isBefore(flexibleCheckInStart)) {
                checkStatus.setCheckStatus((byte) 0);
            } else {
                checkStatus.setCheckStatus((byte) 1);
            }
            createReqVO.setCheckInStatus(checkStatus.getCheckStatus());
            createReqVO.setAttStatus((byte) 1);
            createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate());
            ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert(createReqVO);

            List<ProjectRecordDO> projectRecordDOS = projectRecordMapper.selectList(new LambdaUpdateWrapper<ProjectRecordDO>()
                    .eq(ProjectRecordDO::getPunchDate, createReqVO.getPunchDate()));
            if (projectRecordDOS != null && projectRecordDOS.size() > 0) {
                projectRecordDOS.forEach(pr -> projectRecordMapper.update(null, new LambdaUpdateWrapper<ProjectRecordDO>()
                        .eq(ProjectRecordDO::getId, pr.getId())
                        .set(ProjectRecordDO::getDeleted, 1)));
            }
            projectRecordMapper.insert(records);
        }
    }

    private void updateHandleFixedPunchAttendance(ProjectRecordUpdateReqVO createReqVO, ProjectAttendanceRuleListBaseVO exchange, LocalDateTime now) {
        if (exchange.getAllopatricStatus() == 0) {
            CheckStatusProject checkStatus = new CheckStatusProject();
            List<RuleVO> ruleList = exchange.getList();
            boolean isWithinRange = false;
            for (RuleVO rule : ruleList) {
                String[] centerPoints = rule.getLatiLong().split("/");
                for (String centerPoint : centerPoints) {
                    String[] coordinates = centerPoint.split(",");
                    double centerLatitude = Double.parseDouble(coordinates[0]);
                    double centerLongitude = Double.parseDouble(coordinates[1]);
                    double distance = calculateDistance(createReqVO.getLatitude(), createReqVO.getLongitude(), centerLatitude, centerLongitude);
                    if (distance <= Double.parseDouble(rule.getPunchRadius())) {
                        isWithinRange = true;
                        break;
                    }
                }
            }
            if (isWithinRange) {
                LocalTime flexibleCheckInEnd = exchange.getFlexibleCheckInEnd();
                if (now.toLocalTime().isAfter(flexibleCheckInEnd)) {
                    checkStatus.setCheckStatus((byte) 0);
                } else {
                    checkStatus.setCheckStatus((byte) 1);
                }
            }

            if (checkStatus.getCheckStatus() == null) {
                throw exception(ATTENDANCE_ROUND_NOT_EXISTS);
            }

            createReqVO.setCheckOutStatus(checkStatus.getCheckStatus());
            createReqVO.setAttStatus((byte) 0);
            ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert1(createReqVO);
            projectRecordMapper.updateById(records);
        } else {
            CheckStatusProject checkStatus = new CheckStatusProject();
            LocalTime flexibleCheckInEnd = exchange.getFlexibleCheckInEnd();
            if (now.toLocalTime().isAfter(flexibleCheckInEnd)) {
                checkStatus.setCheckStatus((byte) 0);
            } else {
                checkStatus.setCheckStatus((byte) 1);
            }
            createReqVO.setCheckOutStatus(checkStatus.getCheckStatus());
            createReqVO.setAttStatus((byte) 0);
            ProjectRecordDO records = ProjectRecordConvert.INSTANCE.convert1(createReqVO);
            projectRecordMapper.updateById(records);
        }
    }

}
