package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 员工工资结算 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SalarySettlementRespVO extends SalarySettlementBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
