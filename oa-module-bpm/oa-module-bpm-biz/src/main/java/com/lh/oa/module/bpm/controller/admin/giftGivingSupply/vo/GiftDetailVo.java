package com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiftDetailVo {
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerName;
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;
    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(description = "总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalPrice;
    @Schema(description = "联系业务", requiredMode = Schema.RequiredMode.REQUIRED)
    private String businessType;
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPhone;
    @Schema(description = "客户地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String customerAddress;
    @Schema(description = "送礼内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String giftContent;
}
