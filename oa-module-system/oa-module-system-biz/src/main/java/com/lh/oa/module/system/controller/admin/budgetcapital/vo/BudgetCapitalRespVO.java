package com.lh.oa.module.system.controller.admin.budgetcapital.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Schema(description = "管理后台 - 资金预算 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetCapitalRespVO extends BudgetCapitalBaseVO {

    @Schema(description = "预算ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建时间")
    private Date createTime;

}
