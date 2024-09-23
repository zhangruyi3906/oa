package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 开票申请单采购信息
 *
 * @author tanghanlin
 * @since 2023-10-21
 */
@Getter
@Setter
@ToString
public class InvoicingApplyPurchaseVo implements Serializable {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "是否老金蝶系统", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean oldJindieSys;

    @Schema(description = "销售订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseOrderNo;

    @Schema(description = "销售金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal purchaseAmount;

}
