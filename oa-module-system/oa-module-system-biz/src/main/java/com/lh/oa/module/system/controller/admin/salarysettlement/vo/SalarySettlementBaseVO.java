package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 员工工资结算 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class SalarySettlementBaseVO {

    @Schema(description = "员工编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工编号不能为空")
    private Long userId;

    @Schema(description = "员工name", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工name不能为空")
    private String userName;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long projectId;

    private String projectName;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;

    private String deptName;

    @Schema(description = "基础工资", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "基础工资不能为空")
    private BigDecimal baseSalary;

    @Schema(description = "技术提成", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal technicalBonus;

    @Schema(description = "奖金提成", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal rewardBonus;

    @Schema(description = "法定扣款", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal statutoryDeduction;

    @Schema(description = "考勤扣款", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal attendanceDeduction;

    @Schema(description = "加班工资", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal overtimeSalary;

    @Schema(description = "实发工资", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal realSalary;

    @Schema(description = "扣款明细")
    private String deductionDetails;

    @Schema(description = "考勤天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "考勤天数不能为空")
    private Integer attendanceDays;

    @Schema(description = "结算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结算日期不能为空")
    private String settlementDate;

}
