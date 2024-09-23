package com.lh.oa.module.system.dal.dataobject.salary;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "system_salary", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDO extends BaseDO {
    @TableId
    private Long id;
    @Schema(description = "部门")
    private String deptName;
    @Schema(description = "姓名")
    private String username;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "入职日期")
    private Date hireDate;
    @Schema(description = "应出勤")
    private Double attendance = 0.0;
    @Schema(description = "实际出勤")
    private Double attendanceDays = 0.0;
    @Schema(description = "核算工资出勤")
    private Double salaryAttendance = 0.0;
    @Schema(description = "综合工资总额")
    private BigDecimal attendanceSalary = BigDecimal.valueOf(0);
    //    @Schema(description = "加班天数")
//    private Double overtimeDays;
    @Schema(description = "工龄")
    private BigDecimal senioritySalary = BigDecimal.valueOf(0);
    @Schema(description = "其他加班")
    private BigDecimal overtimeSalary = BigDecimal.valueOf(0);
    @Schema(description = "方量")
    private BigDecimal quantitySalary = BigDecimal.valueOf(0);
    @Schema(description = "绩效金额")
    private BigDecimal performance = BigDecimal.valueOf(0);
    @Schema(description = "年休工资")
    private BigDecimal annualLeaveSalary = BigDecimal.valueOf(0);
    @Schema(description = "病假工资")
    private BigDecimal sickLeaveSalary = BigDecimal.valueOf(0);
    @Schema(description = "绩效奖励")
    private BigDecimal performanceSalary = BigDecimal.valueOf(0);
    @Schema(description = "其他补助")
    private BigDecimal subsidies = BigDecimal.valueOf(0);
    @Schema(description = "应付工资")
    private BigDecimal shouldSalary = BigDecimal.valueOf(0);
    @Schema(description = "迟到/早退/漏签扣款")
    private BigDecimal attendanceDeduction = BigDecimal.valueOf(0);
    @Schema(description = "其他(考核、微博扣款)")
    private BigDecimal assessmentDeduction = BigDecimal.valueOf(0);
    //    @Schema(description = "公积金扣款")
//    private BigDecimal accumulationFund;
    @Schema(description = "社保")
    private BigDecimal socialSecurity = BigDecimal.valueOf(0);
    @Schema(description = "个税")
    private BigDecimal personalTax = BigDecimal.valueOf(0);
    @Schema(description = "借支")
    private BigDecimal borrowing = BigDecimal.valueOf(0);
    @Schema(description = "其他")
    private BigDecimal otherDeduction = BigDecimal.valueOf(0);
    @Schema(description = "实付工资")
    private BigDecimal realSalary = BigDecimal.valueOf(0);
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "月份")
    private String month;
    @Schema(description = "年")
    private String year;

}
