package com.lh.oa.module.system.controller.admin.project.vo;

import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 项目创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectCreateReqVO extends ProjectBaseVO {
    @Schema(description = "项目来源")
    private ProjectSourceEnum source;
}
