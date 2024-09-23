package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * 打卡规则（部门） Excel VO
 *
 * @author
 */
@Data
public class AttendanceRuleExcelVO {

    @ExcelProperty("规则ID")
    private Long id;

    @ExcelProperty("部门ID")
    private Long deptId;


    @ExcelProperty("打卡半径（米）")
    private Object punchRadius;

    @ExcelProperty("迟到限制时间")
    private LocalTime checkInLimit;

    @ExcelProperty("上班时间")
    private LocalTime flexibleCheckInStart;

    @ExcelProperty("下班时间")
    private LocalTime flexibleCheckInEnd;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("打卡经度纬度")
    private String latiLong;

    @ExcelProperty("打卡纬度")
    private Object longitude;

}
