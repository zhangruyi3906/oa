package com.lh.oa.module.system.controller.admin.salary.salary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SalaryBaseVO {
    @Schema(description = "部门")
    private String deptName;
    @Schema(description = "姓名")
    private String username;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "入职日期")
    private Date hireDate;
    @Schema(description = "应出勤")
    private Double attendance;
    @Schema(description = "实际出勤")
    private Double attendanceDays;
    @Schema(description = "核算工资出勤")
    private Double salaryAttendance;
    @Schema(description = "综合工资总额")
    private BigDecimal attendanceSalary;
//    @Schema(description = "加班天数")
//    private Double overtimeDays;
    @Schema(description = "工龄")
    private BigDecimal senioritySalary;
    @Schema(description = "其他加班")
    private BigDecimal overtimeSalary;
    @Schema(description = "方量")
    private BigDecimal quantitySalary;
    @Schema(description = "绩效金额")
    private BigDecimal performance;
    @Schema(description = "年休工资")
    private BigDecimal annualLeaveSalary;
    @Schema(description = "病假工资")
    private BigDecimal sickLeaveSalary;
    @Schema(description = "绩效奖励")
    private BigDecimal performanceSalary;
    @Schema(description = "其他补助")
    private BigDecimal subsidies;
    @Schema(description = "应付工资")
    private BigDecimal shouldSalary;
    @Schema(description = "迟到/早退/漏签扣款")
    private BigDecimal attendanceDeduction;
    @Schema(description = "其他(考核、微博扣款)")
    private BigDecimal assessmentDeduction;
//    @Schema(description = "公积金扣款")
//    private BigDecimal accumulationFund;
    @Schema(description = "社保")
    private BigDecimal socialSecurity;
    @Schema(description = "个税")
    private BigDecimal personalTax;
    @Schema(description = "借支")
    private BigDecimal borrowing;
    @Schema(description = "其他")
    private BigDecimal otherDeduction;
    @Schema(description = "实付工资")
    private BigDecimal realSalary;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "月份")
    private String month;
    @Schema(description = "年")
    private String year;
}
