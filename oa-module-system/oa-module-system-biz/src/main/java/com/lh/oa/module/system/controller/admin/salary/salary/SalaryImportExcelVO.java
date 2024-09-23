package com.lh.oa.module.system.controller.admin.salary.salary;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@HeadStyle(fillForegroundColor = 1)
@HeadFontStyle(color = 8)
@AllArgsConstructor
@NoArgsConstructor
@ColumnWidth(10)
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class SalaryImportExcelVO {
    @ExcelProperty(value = {"安能达工资条导入模板","序号"})
    private String orderNumber;
    @ExcelProperty(value = {"安能达工资条导入模板","部门"})
    private String deptName;

    @ExcelProperty(value = {"安能达工资条导入模板", "姓名"})
    private String username;

    @ExcelProperty(value = {"安能达工资条导入模板", "手机号"})
    private String mobile;
    @ExcelProperty(value = {"安能达工资条导入模板", "入职日期"})
    private Date hireDate;
    @ExcelProperty(value = {"安能达工资条导入模板","考勤情况", "应出勤"})
    private Double attendance;
    @ExcelProperty(value = {"安能达工资条导入模板","考勤情况","实际出勤"})
    private Double attendanceDays;
    @ExcelProperty(value = {"安能达工资条导入模板","考勤情况","核算工资出勤"})
    private BigDecimal salaryAttendance;

    @ExcelProperty(value = {"安能达工资条导入模板","综合工资总额"})
    private BigDecimal attendanceSalary;


    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","工龄"})
    private BigDecimal senioritySalary;
//    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","加班天数"})
////    private Double overtimeDays;


    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","其他加班"})
    private BigDecimal overtimeSalary;

    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","方量"})
    private BigDecimal quantitySalary;

    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","绩效金额"})
    private BigDecimal performance;
    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","年休工资"})
    private BigDecimal annualLeaveSalary;
    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","病假工资"})
    private BigDecimal sickLeaveSalary;
    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","绩效奖励"})
    private BigDecimal performanceSalary;
    @ExcelProperty(value = {"安能达工资条导入模板","其他工资","其他补助"})
    private BigDecimal subsidies;
    @ExcelProperty(value = {"安能达工资条导入模板","应付工资"})
    private BigDecimal shouldSalary;

//    @ExcelProperty(value = {"安能达工资条导入模板","月度考核"})
//
//    private BigDecimal assessment;


    @ExcelProperty(value = {"安能达工资条导入模板","应扣款","迟到/早退/漏签扣款"})
    private BigDecimal attendanceDeduction;

//    @ExcelProperty(value = {"安能达工资条导入模板","宿舍扣款"})
//
//    private BigDecimal dormitoryDeduction;

    @ExcelProperty(value = {"安能达工资条导入模板","应扣款","其他(考核、微博扣款)"})
    private BigDecimal assessmentDeduction;

//    @ExcelProperty(value = {"安能达工资条导入模板","公积金扣款"})
//
//    private BigDecimal accumulationFund;

    @ExcelProperty(value = {"安能达工资条导入模板","应扣款","社保"})
    private BigDecimal socialSecurity;

    @ExcelProperty(value = {"安能达工资条导入模板","应扣款","个税"})
    private BigDecimal personalTax;

    @ExcelProperty(value = {"安能达工资条导入模板","应扣款","借支"})
    private BigDecimal borrowing;

    @ExcelProperty(value = {"安能达工资条导入模板", "应扣款","其他"})
    private BigDecimal otherDeduction;

    @ExcelProperty(value = {"月份","实付工资"})
    private BigDecimal realSalary;

    @ExcelProperty(value = {"例：2022-01","备注"})
    private String remark;

    @ExcelProperty("提示信息")
    @ContentStyle(wrapped = BooleanEnum.TRUE)
    private String msg = "所有日期的格式：年-月-日，例如：2023-01-01；\n导入失败时失败信息会回写在这里，请勿主动填写";

    public Boolean isNotEmpty() {
        return StringUtils.isNotBlank(this.getUsername()) || StringUtils.isNotBlank(this.getMobile());
    }
}
