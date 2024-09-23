package com.lh.oa.module.system.full.service.attendance;

import com.lh.oa.module.system.full.entity.attandance.SysAttendanceRuleEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface SysAttendanceRuleService {

    void saveAttendanceRule(SysAttendanceRuleEntity attendanceRule, int userId);

    SysAttendanceRuleEntity getAttendanceRuleById(int id);

    List<SysAttendanceRuleEntity> getAttendanceRuleList(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType, Pageable pageable);

    int getAttendanceRuleCount(String name, String deptName, String projectName, AttendanceTypeEnum attendanceType);

    void deleteAttendanceRule(String ids, int userId);

    SysAttendanceRuleEntity getAttendanceDistanceRule(int deptId, int projectId, int userId);

    SysAttendanceRuleEntity getAttendanceDistanceRule(int deptId, List<Integer> projectIds, int userId, BigDecimal longitude, BigDecimal latitude);

    SysAttendanceRuleEntity getAttendanceRuleAndRecord(int deptId, int projectId, int userId, int attendanceDate, BigDecimal longitude, BigDecimal latitude);

    List<SysAttendanceRuleEntity> getAttendanceRuleByProjectIds(Set<Integer> projectIds);

    /**
     * 发送固定考勤提醒消息
     */
    void pushRegularAttendanceRemindMessage();

}
