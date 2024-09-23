package com.lh.oa.module.system.service.record;

import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.framework.security.core.LoginUser;
import com.lh.oa.framework.security.core.util.SecurityFrameworkUtils;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.DeptRuleListVO;
import com.lh.oa.module.system.controller.admin.attendanceRule.vo.DeptRuleVO;
import com.lh.oa.module.system.controller.admin.record.vo.*;
import com.lh.oa.module.system.convert.record.RecordConvert;
import com.lh.oa.module.system.dal.dataobject.attendanceRule.AttendanceRuleDO;
import com.lh.oa.module.system.dal.dataobject.holidayInfo.HolidayInfoDO;
import com.lh.oa.module.system.dal.dataobject.record.RecordDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import com.lh.oa.module.system.dal.mysql.attendanceRule.AttendanceRuleMapper;
import com.lh.oa.module.system.dal.mysql.holidayInfo.HolidayInfoMapper;
import com.lh.oa.module.system.dal.mysql.record.RecordMapper;
import com.lh.oa.module.system.dal.mysql.user.AdminUserMapper;
import com.lh.oa.module.system.controller.admin.record.vo.*;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.lh.oa.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 *
 */
@Service
@Validated
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private HolidayInfoMapper mapper;
    @Resource
    private AdminUserMapper userMapper;

    @Resource
    private AttendanceRuleMapper attendanceRuleMapper;

    @Override
    public Boolean createRecord(RecordCreateReqVO createReqVO) {

        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (loginUser != null) {
            Long loginUserId = loginUser.getId();
            AdminUserDO adminUserDO = userMapper.selectById(loginUserId);
            Long deptId = adminUserDO.getDeptId();
            List<AttendanceRuleDO> attendanceRuleDO = attendanceRuleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>()
                    .eq(AttendanceRuleDO::getDeptId, deptId));
            createReqVO.setUserId(loginUserId);
            createReqVO.setUserName(adminUserDO.getNickname());
            if (attendanceRuleDO.isEmpty()) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_RULE_NOT_EXISTS);
            }
            DeptRuleListVO exchange = exchange(attendanceRuleDO);
            isDepAttendance(createReqVO, exchange);
            return true;
        }
        return false;
    }


    @Override
    public void updateRecord(RecordUpdateReqVO updateReqVO) {
        validateRecordExists(updateReqVO.getId());
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        assert loginUser != null;
        Long loginUserId = loginUser.getId();
        Long deptId = userMapper.selectById(loginUserId).getDeptId();
        List<AttendanceRuleDO> attendanceRuleDO = attendanceRuleMapper.selectList(new LambdaQueryWrapperX<AttendanceRuleDO>()
                .eq(AttendanceRuleDO::getDeptId, deptId));
        DeptRuleListVO exchange = exchange(attendanceRuleDO);
        updateHandleFixedPunchAttendance(updateReqVO,exchange,updateReqVO.getCheckOutTime());


    }

    @Override
    public void deleteRecord(Long id) {
        validateRecordExists(id);
        recordMapper.deleteById(id);
    }

    private void validateRecordExists(Long id) {
        if (recordMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.RECORD_NOT_EXISTS);
        }
    }

    @Override
    public RecordDO getRecord(Long id) {
        return recordMapper.selectById(id);
    }

    @Override
    public List<RecordDO> getRecordList(Collection<Long> ids) {
        return recordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<RecordDO> getRecordPage(RecordPageReqVO pageReqVO) {
        return recordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RecordDO> getRecordList(RecordExportReqVO exportReqVO) {
        return recordMapper.selectList(exportReqVO);
    }

    private void isDepAttendance(RecordCreateReqVO createReqVO, DeptRuleListVO exchange) {
        LocalDateTime now = createReqVO.getCheckInTime();
        int value = now.getDayOfWeek().getValue();
        if (exchange.getSyncHolidays() == 1) {
            String[] workDaysArray = exchange.getWorkDays().split(",");
            List<String> workDaysList = Arrays.asList(workDaysArray);
            boolean isWorkDay = workDaysList.contains(String.valueOf(value));

            if (isWorkDay) {
                handleFixedPunchAttendance(createReqVO, exchange, now);
            } else {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.HOLIDAY_NOT_RECORD);
            }
        } else {
            int formattedDate = now.getYear() * 10000 + now.getMonthValue() * 100 + now.getDayOfMonth();
            Integer workday = mapper.selectOne(new LambdaQueryWrapperX<HolidayInfoDO>().eq(HolidayInfoDO::getDate, formattedDate)).getWorkday();

            if (workday == 2) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.HOLIDAY_NOT_RECORD);
            } else {
                handleFixedPunchAttendance(createReqVO, exchange, now);
            }
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

    private DeptRuleListVO exchange(List<AttendanceRuleDO> list) {
        DeptRuleListVO baseVO = new DeptRuleListVO();
        AttendanceRuleDO projectAttendanceRuleDO = list.get(0);
        baseVO.setDeptId(projectAttendanceRuleDO.getDeptId());
        baseVO.setDeptName(projectAttendanceRuleDO.getDeptName());
        baseVO.setPunchTypeName(projectAttendanceRuleDO.getPunchTypeName());
        baseVO.setSyncHolidays(projectAttendanceRuleDO.getSyncHolidays());
        baseVO.setWorkDays(projectAttendanceRuleDO.getWorkDays());
        baseVO.setFlexibleCheckInStart(projectAttendanceRuleDO.getFlexibleCheckInStart());
        baseVO.setFlexibleCheckInEnd(projectAttendanceRuleDO.getFlexibleCheckInEnd());
        List<DeptRuleVO> ruleVOList = list.stream()
                .map(s -> new DeptRuleVO(s.getPunchRadius(), s.getDescription(), s.getLatiLong()))
                .collect(Collectors.toList());
        baseVO.setList(ruleVOList);
        return baseVO;
    }

    private void handleFixedPunchAttendance(RecordCreateReqVO createReqVO, DeptRuleListVO exchange, LocalDateTime now) {
        CheckStatusProject checkStatus = new CheckStatusProject();
        List<DeptRuleVO> ruleList = exchange.getList();
        boolean isWithinRange = false;
        for (DeptRuleVO rule : ruleList) {
            String[] centerPoints = rule.getLatiLong().split("/");
            for (String centerPoint : centerPoints) {
                String[] coordinates = centerPoint.split(",");
                double centerLatitude = Double.parseDouble(coordinates[0]);
                double centerLongitude = Double.parseDouble(coordinates[1]);
                double distance = calculateDistance(createReqVO.getLatitude(), createReqVO.getLongitude(), centerLatitude, centerLongitude);
                if (distance <= (Double) rule.getPunchRadius()) {
                    isWithinRange = true;
                    break;
                }
            }
        }
        if (isWithinRange) {
            LocalTime flexibleCheckInStart = exchange.getFlexibleCheckInStart();
            if (now.toLocalTime().isBefore(flexibleCheckInStart)) {
                checkStatus.setCheckStatus((byte) 0);
            } else {
                checkStatus.setCheckStatus((byte) 1);
            }
        }
        if (checkStatus.getCheckStatus() == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_ROUND_NOT_EXISTS);
        }
        createReqVO.setCheckInStatus(checkStatus.getCheckStatus());
        createReqVO.setDeptId(exchange.getDeptId());
        createReqVO.setDeptName(exchange.getDeptName());
        createReqVO.setAttStatus((byte) 0);
        createReqVO.setPunchDate(createReqVO.getCheckInTime().toLocalDate());
        RecordDO records = RecordConvert.INSTANCE.convert(createReqVO);
        recordMapper.insert(records);

    }
    private void updateHandleFixedPunchAttendance(RecordUpdateReqVO createReqVO, DeptRuleListVO exchange, LocalDateTime now){
            CheckStatusProject checkStatus = new CheckStatusProject();
            List<DeptRuleVO> ruleList = exchange.getList();
            boolean isWithinRange = false;
            for (DeptRuleVO rule : ruleList) {
                String[] centerPoints = rule.getLatiLong().split("/");
                for (String centerPoint : centerPoints) {
                    String[] coordinates = centerPoint.split(",");
                    double centerLatitude = Double.parseDouble(coordinates[0]);
                    double centerLongitude = Double.parseDouble(coordinates[1]);
                    double distance = calculateDistance(createReqVO.getLatitude(), createReqVO.getLongitude(), centerLatitude, centerLongitude);
                    if (distance <= (Double)rule.getPunchRadius()) {
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
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ATTENDANCE_ROUND_NOT_EXISTS);
            }

            createReqVO.setCheckOutStatus(checkStatus.getCheckStatus());
            createReqVO.setAttStatus((byte) 0);
            RecordDO records = RecordConvert.INSTANCE.convert(createReqVO);
            recordMapper.updateById(records);

    }

}
