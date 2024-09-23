package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 打卡规则（项目）更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectAttendanceRuleUpdateReqVO extends ProjectAttendanceRuleBaseVO {

    @Schema(description = "项目规则id", required = true)
    private Long id;

}
