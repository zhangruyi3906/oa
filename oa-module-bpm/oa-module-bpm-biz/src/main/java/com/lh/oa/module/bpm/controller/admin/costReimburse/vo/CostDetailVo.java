package com.lh.oa.module.bpm.controller.admin.costReimburse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CostDetailVo {
    @Schema(description = "科目", requiredMode = Schema.RequiredMode.REQUIRED)
    private String subject;
    @Schema(description = "是否为X事业部/售后部", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isX;
    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;
    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deviceName;
    @Schema(description = "规格型号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String model;
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;
    @Schema(description = "摘要", requiredMode = Schema.RequiredMode.REQUIRED)
    private String precis;
    @Schema(description = "报销金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal reimburse;
}
