package com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PurchaseDetailVo {
//    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
//    private Long id;
    @Schema(description = "物品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String goodsName;
    @Schema(description = "型号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String model;
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer number;
    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;
    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(description = "总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalPrice;
    @Schema(description = "购回日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date buyDate;
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String remark;
    @Schema(description = "附件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annex;
}
