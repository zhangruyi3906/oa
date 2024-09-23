package com.lh.oa.module.system.controller.admin.salary.salary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 用户创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SalaryCreateVO extends SalaryBaseVO{
}
