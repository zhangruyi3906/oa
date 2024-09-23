package com.lh.oa.module.system.controller.admin.worklog.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 员工工作日志创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkLogCreateReqVO extends WorkLogBaseVO {

}
