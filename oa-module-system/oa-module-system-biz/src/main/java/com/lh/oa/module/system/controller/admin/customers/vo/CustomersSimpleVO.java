package com.lh.oa.module.system.controller.admin.customers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "客户精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomersSimpleVO {

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "公司名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;

}