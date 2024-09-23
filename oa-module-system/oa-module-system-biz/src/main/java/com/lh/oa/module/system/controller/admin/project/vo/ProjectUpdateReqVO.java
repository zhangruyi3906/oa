package com.lh.oa.module.system.controller.admin.project.vo;

import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 项目更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectUpdateReqVO extends ProjectBaseVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    @Schema(description = "置顶")
    private String isTop;
    @Schema(description = "项目来源")
    private ProjectSourceEnum source;


}
