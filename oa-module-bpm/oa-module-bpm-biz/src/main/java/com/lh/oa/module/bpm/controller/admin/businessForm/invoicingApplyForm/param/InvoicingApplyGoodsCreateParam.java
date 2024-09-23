package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
public class InvoicingApplyGoodsCreateParam implements Serializable {

    @Schema(description = "货物名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "申请人不能为空")
    private String name;

    @Schema(description = "规格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "规格不能为空")
    private String spec;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "单位不能为空")
    private String unit;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "数量不能为空")
    private Integer num;

    @Schema(description = "含税单价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "含税单价不能为空")
    private BigDecimal taxPrice;

    @Schema(description = "含税金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "含税金额不能为空")
    private BigDecimal taxAmount;

}
