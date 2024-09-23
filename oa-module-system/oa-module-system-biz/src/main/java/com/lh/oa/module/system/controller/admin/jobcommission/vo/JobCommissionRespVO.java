package com.lh.oa.module.system.controller.admin.jobcommission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 项目工种提成 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobCommissionRespVO extends JobCommissionBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
