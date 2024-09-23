package com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfficeVO {
    @Schema(description = "物品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String officeListName;
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long listNumber;
    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;
    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;
    @Schema(description = "总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal allPrice;
    @Schema(description = "购回日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date purchaseDate;
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reason;
    private String url;
}
