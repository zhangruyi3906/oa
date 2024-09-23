package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 开票申请单货物信息
 *
 * @author tanghanlin
 * @since 2023-10-21
 */
@Getter
@Setter
@ToString
public class InvoicingApplyGoodsVo implements Serializable {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "货物名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "规格", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spec;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer num;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxPrice;

    @Schema(description = "含税金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxAmount;

}
