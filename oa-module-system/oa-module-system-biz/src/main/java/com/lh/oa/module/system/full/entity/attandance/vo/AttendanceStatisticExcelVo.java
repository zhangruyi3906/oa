package com.lh.oa.module.system.full.entity.attandance.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AttendanceStatisticExcelVo  {
    @ExcelProperty({"员工"})
    private String userName;
    @ExcelProperty({"部门"})
    private String deptName;
    @ExcelProperty({"项目"})
    private String projectName;
    @ExcelIgnore
    private String hireDateStr;//入职时间
    @ExcelProperty({"考勤情况", "考勤月份"})
    private String attendanceMonthStr;
    @ExcelProperty({"考勤情况", "应出勤（天）"})
    private Integer attendanceMonthDayCount;
    @ExcelProperty({"考勤情况", "实际出勤（天）"})
    private Integer attendanceNormalCount;//出勤（天）
    @ExcelProperty({"考勤情况", "请假（天）"})
    private BigDecimal attendanceLeaveCount;//请假（天）
    @ExcelProperty({"考勤情况", "迟到（次）"})
    private Integer attendanceLateCount;//迟到（次）
    @ExcelProperty({"考勤情况", "早退（次）"})
    private Integer attendanceEarlyCount;//早退（次）
    @ExcelProperty({"考勤情况", "漏签（次）"})
    private Integer attendanceMissCount;//漏签（次）
    @ExcelProperty({"考勤情况", "加班（小时）"})
    private BigDecimal attendanceAddCount = BigDecimal.ZERO;//加班（小时）
    @ExcelProperty({"考勤情况", "出差（天）"})
    private BigDecimal attendanceTravelCount = BigDecimal.ZERO;//出差（天）
    @ExcelProperty({"考勤情况", "旷工（天）"})
    private Integer attendanceAbsentCount;//旷工（次）


}
