package com.lh.oa.module.system.controller.admin.salary.salary;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@HeadStyle(fillForegroundColor = 1)
@HeadFontStyle(color = 8)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class SalaryExportExcelVO {
    @ExcelProperty(value = {"部门"})
    private String deptName;

    @ExcelProperty(value = { "姓名"})

    private String username;

    @ExcelProperty(value = { "手机号"})

    private String mobile;
    @ExcelProperty(value = { "入职日期"})

    private Date hireDate;

    @ExcelProperty(value = {"考勤情况", "应出勤"})

    private Double attendance;
    @ExcelProperty(value = {"考勤情况","实际出勤"})

    private Double attendanceDays;
    @ExcelProperty(value = {"考勤情况","核算工资出勤"})

    private BigDecimal salaryAttendance;

    @ExcelProperty(value = {"综合工资总额"})

    private BigDecimal attendanceSalary;


    @ExcelProperty(value = {"其他工资","工龄"})

    private BigDecimal senioritySalary;
//    @ExcelProperty(value = {"其他工资","加班天数"})
//    private Double overtimeDays;


    @ExcelProperty(value = {"其他工资","其他加班"})

    private BigDecimal overtimeSalary;

    @ExcelProperty(value = {"其他工资","方量"})

    private BigDecimal quantitySalary;

    @ExcelProperty(value = {"其他工资","绩效金额"})

    private BigDecimal performance;
    @ExcelProperty(value = {"其他工资","年休工资"})

    private BigDecimal annualLeaveSalary;
    @ExcelProperty(value = {"其他工资","病假工资"})

    private BigDecimal sickLeaveSalary;
    @ExcelProperty(value = {"其他工资","绩效奖励"})

    private BigDecimal performanceSalary;
    @ExcelProperty(value = {"其他工资","其他补助"})

    private BigDecimal subsidies;
    @ExcelProperty(value = {"应付工资"})
    private BigDecimal shouldSalary;

//    @ExcelProperty(value = {"月度考核"})
//    private BigDecimal assessment;


    @ExcelProperty(value = {"应扣款","迟到/早退/漏签扣款"})

    private BigDecimal attendanceDeduction;

//    @ExcelProperty(value = {"宿舍扣款"})
//    private BigDecimal dormitoryDeduction;

    @ExcelProperty(value = {"应扣款","其他(考核、微博扣款)"})

    private BigDecimal assessmentDeduction;

//    @ExcelProperty(value = {"公积金扣款"})
//    private BigDecimal accumulationFund;

    @ExcelProperty(value = {"应扣款","社保"})
    private BigDecimal socialSecurity;

    @ExcelProperty(value = {"应扣款","个税"})
    private BigDecimal personalTax;

    @ExcelProperty(value = {"应扣款","借支"})
    private BigDecimal borrowing;

    @ExcelProperty(value = { "应扣款","其他"})
    private BigDecimal otherDeduction;

    @ExcelProperty(value = {"实付工资"})
    private BigDecimal realSalary;

    @ExcelProperty(value = {"备注"})
    private String remark;

    @ExcelProperty("月份")
    private String month;
}
