package com.lh.oa.module.system.full.entity.attandance.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttendanceStatisticInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String userName;
    private int deptId;
    private String deptName;
    private int hireDate;//入职时间

    /**
     * 考勤月份
     */
    private int attendanceMonth;
    /**
     * 考勤天数
     */
    private int attendanceMonthDayCount;
    /**
     * 出勤（天）
     */
    private Integer attendanceNormalCount = 0;

    /**
     * 请假（天）
     */
    private BigDecimal attendanceLeaveCount = BigDecimal.ZERO;

    /**
     * 迟到（次）
     */
    private Integer attendanceLateCount = 0;

    /**
     * 早退（次）
     */
    private Integer attendanceEarlyCount = 0;

    /**
     * 旷工（次）
     */
    private Integer attendanceAbsentCount = 0;

    /**
     * 漏签（次）
     */
    private Integer attendanceMissCount = 0;

    /**
     * 加班（小时）
     */
    private BigDecimal attendanceAddCount = BigDecimal.ZERO;

    /**
     * 出差（天）
     */
    private BigDecimal attendanceTravelCount = BigDecimal.ZERO;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 入职日期
     */
    private String hireDateStr;

}