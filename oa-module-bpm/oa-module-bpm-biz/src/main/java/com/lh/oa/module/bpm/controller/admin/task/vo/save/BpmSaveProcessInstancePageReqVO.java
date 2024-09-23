package com.lh.oa.module.bpm.controller.admin.task.vo.save;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 保存和撤回流程实例的分页 Item Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmSaveProcessInstancePageReqVO extends PageParam {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "模型id")
    private String modelId;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "表单的配置")
    private String formConf;

    @Schema(description = "表单值")
    private String formVariables;
}