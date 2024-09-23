package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工工资结算 Excel VO
 *
 * @author
 */
@Data
public class SalarySettlementExcelVO {

    @ExcelProperty("记录编号")
    private Long id;

    @ExcelProperty("员工编号")
    private Long userId;

    @ExcelProperty("员工姓名")
    private String userName;

    //保留字段
//    @ExcelProperty("项目编号")
//    private Long projectId;

//    @ExcelProperty("部门编号")
//    private Long deptId;

    @ExcelProperty("部门名字")
    private String deptName;

    @ExcelProperty("基础工资")
    private BigDecimal baseSalary;

//    @ExcelProperty("技术提成")
//    private BigDecimal technicalBonus;
//
//    @ExcelProperty("奖金提成")
//    private BigDecimal rewardBonus;

    @ExcelProperty("法定扣款")
    private BigDecimal statutoryDeduction;

    @ExcelProperty("考勤扣款")
    private BigDecimal attendanceDeduction;

//    @ExcelProperty("加班工资")
//    private BigDecimal overtimeSalary;

    @ExcelProperty("实发工资")
    private BigDecimal realSalary;

    @ExcelProperty("扣款明细")
    private String deductionDetails;

    @ExcelProperty("考勤天数")
    private Integer attendanceDays;

    @ExcelProperty("结算日期")
    private String settlementDate;

}
