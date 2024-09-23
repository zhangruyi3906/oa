package com.lh.oa.module.system.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 项目精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSimpleRespVO {
    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private Long id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String name;
}
