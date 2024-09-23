package com.lh.oa.module.system.controller.admin.worklog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 员工工作日志 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkLogRespVO extends WorkLogBaseVO {

    @Schema(description = "日志ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
