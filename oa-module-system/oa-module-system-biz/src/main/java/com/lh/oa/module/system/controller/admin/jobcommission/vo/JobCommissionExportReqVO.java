package com.lh.oa.module.system.controller.admin.jobcommission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 项目工种提成 Excel 导出 Request VO，参数和 JobCommissionPageReqVO 是一致的")
@Data
public class JobCommissionExportReqVO {

    @Schema(description = "项目编号")
    private Long projectId;

    @Schema(description = "工种id")
    private Long jobId;

    @Schema(description = "基础提成")
    private BigDecimal baseCommission;

    @Schema(description = "奖励提成")
    private BigDecimal bonusCommission;

}
