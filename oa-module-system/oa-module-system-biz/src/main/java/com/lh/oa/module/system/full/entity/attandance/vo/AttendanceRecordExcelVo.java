package com.lh.oa.module.system.full.entity.attandance.vo;

import com.lh.oa.module.system.full.enums.attendance.AttendanceClockStatusEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceStatusEnum;
import com.lh.oa.module.system.full.enums.attendance.AttendanceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceRecordExcelVo {

    private String userName;
    private String deptName;
    private String projectName;
    private String attendanceDateStr;

    private String attendanceTypeVal;
    private String attendanceStatusVal;

    private String clockInTime;
    private String clockInPosition;
    private String clockInLongitudeLatitude;
    private String clockInStatusVal;

    private String clockOffTime;
    private String clockOffPosition;
    private String clockOffLongitudeLatitude;
    private String clockOffStatusVal;

}