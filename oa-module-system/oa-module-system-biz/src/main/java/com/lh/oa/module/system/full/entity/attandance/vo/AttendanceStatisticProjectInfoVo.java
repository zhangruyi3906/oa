package com.lh.oa.module.system.full.entity.attandance.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceStatisticProjectInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String userName;
    private int projectId;
    private String projectName;
    private int hireDate;//入职时间

    private int attendanceMonth;
    private int attendanceMonthDayCount;
    private Integer attendanceNormalCount;//出勤（天）
    private Integer attendanceLeaveCount;//请假（小时）
    private Integer attendanceLateCount;//迟到（次）
    private Integer attendanceEarlyCount;//早退（次）
    //    private Integer attendanceAbsentCount;//旷工（次）
    private Integer attendanceMissCount;//漏签（次）
}
