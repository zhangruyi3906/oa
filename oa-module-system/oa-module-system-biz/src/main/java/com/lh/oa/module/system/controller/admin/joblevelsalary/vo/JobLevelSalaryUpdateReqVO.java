package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 员工工种等级基础工资更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobLevelSalaryUpdateReqVO extends JobLevelSalaryBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "记录编号不能为空")
    private Long id;

}
