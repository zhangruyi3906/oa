package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 员工工种等级基础工资创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobLevelSalaryCreateReqVO extends JobLevelSalaryBaseVO {

}
