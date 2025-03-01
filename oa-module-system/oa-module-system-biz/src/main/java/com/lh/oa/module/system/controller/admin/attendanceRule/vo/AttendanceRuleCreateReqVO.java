package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 打卡规则（部门）创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttendanceRuleCreateReqVO extends AttendanceRuleBaseVO {

}
