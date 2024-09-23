package com.lh.oa.module.bpm.controller.admin.definition.vo.process;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 流程定义列表 Request VO")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BpmProcessDefinitionListReqVO extends PageParam {

    @Schema(description = "中断状态,参见 SuspensionState 枚举", example = "1")
    private Integer suspensionState;

    private String id;

    @Schema(description = "流程名称 模糊查询", example = "1")
    private String name;
    @Schema(description = "流程类型id", example = "1")
    private String processTypeId;
}