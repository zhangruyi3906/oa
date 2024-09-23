package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 资金预算申请更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetApplicationUpdateReqVO extends BudgetApplicationBaseVO {

    @Schema(description = "预算申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预算申请ID不能为空")
    private Long id;

}
