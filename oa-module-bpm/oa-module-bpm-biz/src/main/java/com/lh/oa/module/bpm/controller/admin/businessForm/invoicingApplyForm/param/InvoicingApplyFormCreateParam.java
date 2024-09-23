package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 开票申请单
 *
 * @author tanghanlin
 * @since 2023/10/21
 */
@Getter
@Setter
@ToString
public class InvoicingApplyFormCreateParam implements Serializable {

    @Schema(description = "开票申请单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "申请人不能为空")
    private Long applyUserId;

    @Schema(description = "是否关联销售员", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否关联销售员不能为空")
    private Boolean relatedSale;

    @Schema(description = "相关销售", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "相关销售不能为空")
    private Long relatedSaleUserId;

    @Schema(description = "是否收款", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否收款不能为空")
    private Boolean received;

    @Schema(description = "开票公司", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开票公司不能为空")
    private Integer applyCompany;

    @Schema(description = "开票种类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开票种类不能为空")
    private Integer formType;

    @Schema(description = "开票金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开票金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "收款详细情况", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收款详细情况不能为空")
    private String receivedDetail;

    @Schema(description = "购买方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "购买方名称不能为空")
    private String purchaseName;

    @Schema(description = "纳税人识别号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "纳税人识别号不能为空")
    @Pattern(regexp = "^[0-9a-zA-Z]{15}|^[0-9a-zA-Z]{18}|^[0-9a-zA-Z]{20}$", message = "请输入正确的纳税人识别号")
    private String purchaseTaxpayerCode;

    @Schema(description = "地址、电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "地址、电话不能为空")
    private String purchaseAddressMobile;

    @Schema(description = "开户行", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "开户行不能为空")
    private String purchaseBank;

    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "银行账号不能为空")
    @Pattern(regexp = "^[0-9 ]{16,23}$", message = "请输入正确的银行账号")
    private String purchaseBankAccount;

    @Schema(description = "销售种类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "销售种类不能为空")
    private Integer saleType;

    @Schema(description = "销售合同url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String saleContractUrl;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "备注不能为空")
    private String remark;

    @Schema(description = "附件url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annexUrl;

    @Schema(description = "销售清单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "销售清单不能为空")
    private List<InvoicingApplyPurchaseCreateParam> purchaseList;

    @Schema(description = "货物清单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "货物清单不能为空")
    private List<InvoicingApplyGoodsCreateParam> goodsList;

}
