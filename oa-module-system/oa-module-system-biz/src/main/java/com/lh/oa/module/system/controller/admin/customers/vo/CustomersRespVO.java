package com.lh.oa.module.system.controller.admin.customers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 客户基础信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomersRespVO extends CustomersBaseVO {

    @Schema(description = "客户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
