package com.lh.oa.module.system.controller.admin.budgetcapital.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 资金预算更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetCapitalUpdateReqVO extends BudgetCapitalBaseVO {

    @Schema(description = "预算ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预算ID不能为空")
    private Long id;

}
