package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 员工工种等级基础工资 Excel 导出 Request VO，参数和 JobLevelSalaryPageReqVO 是一致的")
@Data
public class JobLevelSalaryExportReqVO {

    @Schema(description = "工种类型")
    private String jobCode;

    @Schema(description = "技术等级")
    private Integer jobLevel;

    @Schema(description = "员工id")
    private Long userId;

    @Schema(description = "基础工资")
    private BigDecimal baseSalary;

}
