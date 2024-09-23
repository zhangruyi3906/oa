package com.lh.oa.module.system.controller.admin.budgetcapital.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 资金预算 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BudgetCapitalBaseVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @Schema(description = "预算周期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预算周期不能为空")
    private String budgetPeriod;

    @Schema(description = "预算类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预算类型不能为空")
    private String budgetType;

    @Schema(description = "预算金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预算金额不能为空")
    private BigDecimal budgetAmount;

    private String projectName;

}
