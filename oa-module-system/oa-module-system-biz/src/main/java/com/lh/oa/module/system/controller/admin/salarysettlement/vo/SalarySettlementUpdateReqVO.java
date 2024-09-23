package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 员工工资结算更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SalarySettlementUpdateReqVO extends SalarySettlementBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "记录编号不能为空")
    private Long id;

}
