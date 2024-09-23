package com.lh.oa.module.bpm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 任务撤回 Request VO")
@Data
public class BpmTaskRevokeReqVO {
    @Schema(description = "流程任务的Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程任务的Id不能为空")
    private String taskId;

    @Schema(description = "流程实例Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程实例Id不能为空")
    private String processInstanceId;
}
