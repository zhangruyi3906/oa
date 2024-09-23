package com.lh.oa.module.bpm.controller.admin.businessForm.invoicingApplyForm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 开票申请单
 *
 * @author tanghanlin
 * @since 2023-10-21
 */
@Getter
@Setter
@ToString
public class InvoicingApplyFormVo implements Serializable {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applyUserName;

    @Schema(description = "是否关联销售员", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean relatedSale;

    @Schema(description = "相关销售", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relatedSaleUserName;

    @Schema(description = "是否收款", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean received;

    @Schema(description = "开票公司", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applyCompany;

    @Schema(description = "开票种类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String formType;

    @Schema(description = "开票金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalAmount;

    @Schema(description = "收款详细情况", requiredMode = Schema.RequiredMode.REQUIRED)
    private String receivedDetail;

    @Schema(description = "购买方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseName;

    @Schema(description = "纳税人识别号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseTaxpayerCode;

    @Schema(description = "地址、电话", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseAddressMobile;

    @Schema(description = "开户行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseBank;

    @Schema(description = "银行账号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String purchaseBankAccount;

    @Schema(description = "销售种类", requiredMode = Schema.RequiredMode.REQUIRED)
    private String saleType;

    @Schema(description = "销售合同url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String saleContractUrl;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String remark;

    @Schema(description = "附件url", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annexUrl;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createTime;

    @Schema(description = "销售清单", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<InvoicingApplyPurchaseVo> purchaseList;

    @Schema(description = "货物清单", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<InvoicingApplyGoodsVo> goodsList;

    @Schema(description = "状态,参见 bpm_process_instance_result 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer result;

    @Schema(description = "流程id")
    private String processInstanceId;

}
