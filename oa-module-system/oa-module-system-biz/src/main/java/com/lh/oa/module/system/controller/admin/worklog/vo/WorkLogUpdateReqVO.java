package com.lh.oa.module.system.controller.admin.worklog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 员工工作日志更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkLogUpdateReqVO extends WorkLogBaseVO {

    @Schema(description = "日志ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "日志ID不能为空")
    private Long id;

}
