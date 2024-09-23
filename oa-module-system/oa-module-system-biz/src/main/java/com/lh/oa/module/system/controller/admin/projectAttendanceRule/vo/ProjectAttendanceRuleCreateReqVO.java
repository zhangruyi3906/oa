package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 打卡规则（项目）创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectAttendanceRuleCreateReqVO extends ProjectAttendanceRuleBaseVO {

}
