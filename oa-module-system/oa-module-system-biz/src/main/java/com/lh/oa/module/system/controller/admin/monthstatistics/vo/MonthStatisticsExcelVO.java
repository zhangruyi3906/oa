package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 考勤月统计 Excel VO
 *
 * @author
 */
@Data
public class MonthStatisticsExcelVO {

    @ExcelProperty("统计编号")
    private Long id;

    @ExcelProperty("员工编号")
    private Long userId;

    @ExcelProperty("项目编号")
    private Long projectId;

    @ExcelProperty("部门编号")
    private Long deptId;

    @ExcelProperty("考勤月份")
    private String attendanceMonth;

    @ExcelProperty("总工作天数")
    private Integer totalWorkingDays;

    @ExcelProperty("实际工作天数")
    private Integer actualWorkingDays;

    @ExcelProperty("总加班小时数")
    private Integer totalOvertimeHours;

    @ExcelProperty("总请假天数")
    private Integer totalLeaveDays;

    @ExcelProperty("总缺勤天数")
    private Integer totalAbsenceDays;

}
