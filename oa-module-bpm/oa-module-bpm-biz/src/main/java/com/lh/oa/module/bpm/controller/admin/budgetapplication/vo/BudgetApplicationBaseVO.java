package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import javax.validation.constraints.*;

/**
 * 资金预算申请 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class BudgetApplicationBaseVO {

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

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer approvalStatus;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

}
