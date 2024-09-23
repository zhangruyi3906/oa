package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 资金预算申请创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BudgetApplicationCreateReqVO extends BudgetApplicationBaseVO {

}
