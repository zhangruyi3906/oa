package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 打卡规则（项目） Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectAttendanceRuleRespVO extends ProjectAttendanceRuleBaseVO {

    @Schema(description = "项目规则id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
