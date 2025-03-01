package com.lh.oa.module.bpm.controller.admin.definition.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 流程模型的创建 Request VO")
@Data
public class BpmModelCreateReqVO {

    @Schema(description = "流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "process_yudao")
    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @Schema(description = "流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "道")
    @NotEmpty(message = "流程名称不能为空")
    private String name;

    @Schema(description = "流程描述", example = "我是描述")
    private String description;

    @Schema(description = "流程类型")
    private String type;

    @Schema(description = "允许使用的部门", example = "64")
    private List<Long> deptIds;
}