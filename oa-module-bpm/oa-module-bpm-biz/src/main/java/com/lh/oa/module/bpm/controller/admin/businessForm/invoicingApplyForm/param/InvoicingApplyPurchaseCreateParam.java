package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
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
public class InvoicingApplyPurchaseCreateParam implements Serializable {

    @Schema(description = "是否老金蝶系统", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean oldJindieSys;

    @Schema(description = "销售订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "销售订单号不能为空")
    private String purchaseOrderNo;

    @Schema(description = "销售金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "销售金额不能为空")
    private BigDecimal purchaseAmount;

}
