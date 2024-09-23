package com.lh.oa.module.bpm.controller.admin.definition.vo.process;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 流程定义 Response VO")
@Data
public class BpmProcessTypeGroupRespVO {

    @Schema(description = "流程类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String processTypeId;

    @Schema(description = "流程类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋道")
    @NotEmpty(message = "流程名称不能为空")
    private String processTypeName;

    @Schema(description = "流程列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private List<BpmProcessDefinitionRespVO> processList;

}