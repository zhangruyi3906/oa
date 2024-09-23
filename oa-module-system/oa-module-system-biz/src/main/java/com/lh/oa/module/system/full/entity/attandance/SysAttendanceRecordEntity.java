package com.lh.oa.module.system.full.entity.attandance;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.attendance.AttendanceStatusEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceClockStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysAttendanceRecordEntity extends BaseEntity {
    private int userId;
    private Integer deptId;
    private Integer projectId;
    private int attendanceRuleId;
    private int attendanceDate;
    private AttendanceTypeEnum attendanceType;
    private AttendanceStatusEnum attendanceStatus;

    private String attendanceDateStr;
    private String clockInTime;
    private String clockInPosition;
    private String clockInLongitudeLatitude;
    private AttendanceClockStatusEnum clockInStatus;
    private String clockInPhotoUrl;
    private String clockOffTime;
    private String clockOffPosition;
    private String clockOffLongitudeLatitude;
    private AttendanceClockStatusEnum clockOffStatus;
    private String clockOffPhotoUrl;

    public String getAttendanceTypeVal() {
        return attendanceType == null ? null : attendanceType.getVal();
    }
    public String getAttendanceStatusVal() {
        return attendanceStatus == null ? null : attendanceStatus.getVal();
    }
    public String getClockInClockStatusVal() {
        return clockInStatus == null ? null : clockInStatus.getVal();
    }
    public String getClockOffClockStatusVal() {
        return clockOffStatus == null ? null : clockOffStatus.getVal();
    }

    private String userName;
    private String deptName;
    private String projectName;

    private SysAttendanceRuleEntity attendanceRule;

}