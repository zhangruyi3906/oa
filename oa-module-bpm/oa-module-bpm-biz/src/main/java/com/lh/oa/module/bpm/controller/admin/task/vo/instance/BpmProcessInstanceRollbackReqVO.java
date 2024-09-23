package com.lh.oa.module.bpm.controller.admin.task.vo.instance;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 流程退回 Request VO")
@Data
public class BpmProcessInstanceRollbackReqVO {
    @Schema(description = "流程任务的Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程任务的Id不能为空")
    private String taskId;

    @Schema(description = "流程退回节点的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程退回节点的编号不能为空")
    private String rollbackId;

    @Schema(description = "流程退回的备注", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "流程退回的备注不能为空")
    private String comment;

    private Integer result;
}
