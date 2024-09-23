package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Schema(description = "管理后台 - 资金预算申请 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetApplicationRespVO extends BudgetApplicationBaseVO {

    @Schema(description = "预算申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建时间")
    private Date createTime;

}
