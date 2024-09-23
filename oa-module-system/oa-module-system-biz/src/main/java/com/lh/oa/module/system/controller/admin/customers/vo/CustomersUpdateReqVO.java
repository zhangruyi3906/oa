package com.lh.oa.module.system.controller.admin.customers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 客户基础信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomersUpdateReqVO extends CustomersBaseVO {

    @Schema(description = "客户id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户id不能为空")
    private Long id;

}
