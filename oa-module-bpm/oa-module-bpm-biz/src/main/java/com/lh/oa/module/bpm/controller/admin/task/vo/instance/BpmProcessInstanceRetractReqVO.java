package com.lh.oa.module.bpm.controller.admin.task.vo.instance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 流程实例的撤回 Request VO")
@Data
public class BpmProcessInstanceRetractReqVO {
    @Schema(description = "流程实例的编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程实例的编号不能为空")
    private String processInstanceId;

    @Schema(description = "撤回原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不请假了！")
    private String reason;

    @Schema(description = "用户id")
    private Long userId;
}
