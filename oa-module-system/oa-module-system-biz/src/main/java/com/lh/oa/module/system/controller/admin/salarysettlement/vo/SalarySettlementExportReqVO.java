package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;


@Schema(description = "管理后台 - 员工工资结算 Excel 导出 Request VO，参数和 SalarySettlementPageReqVO 是一致的")
@Data
public class SalarySettlementExportReqVO {

    @Schema(description = "员工编号")
    private Long userId;

    @Schema(description = "项目编号")
    private Long projectId;

    @Schema(description = "部门编号")
    private Long deptId;

    @Schema(description = "基础工资")
    private BigDecimal baseSalary;

    @Schema(description = "技术提成")
    private BigDecimal technicalBonus;

    @Schema(description = "奖金提成")
    private BigDecimal rewardBonus;

    @Schema(description = "法定扣款")
    private BigDecimal statutoryDeduction;

    @Schema(description = "考勤扣款")
    private BigDecimal attendanceDeduction;

    @Schema(description = "加班工资")
    private BigDecimal overtimeSalary;

    @Schema(description = "实发工资")
    private BigDecimal realSalary;


    @Schema(description = "扣款明细")
    private String deductionDetails;

    @Schema(description = "考勤天数")
    private Integer attendanceDays;

    @Schema(description = "结算日期")
    private String settlementDate;

}