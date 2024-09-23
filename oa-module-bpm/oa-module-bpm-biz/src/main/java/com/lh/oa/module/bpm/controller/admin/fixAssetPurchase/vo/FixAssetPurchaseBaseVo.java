package com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author yangsheng
 * @since
 */
@Schema(description = "管理后台 - 固定资产申购 BaseVo")
@Data
@ToString(callSuper = true)
public class FixAssetPurchaseBaseVo {
    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    @Schema(description = "办公地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String place;
    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(description = "申请人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;
    @Schema(description = "申请部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date supportedDate;
    @Schema(description = "申购原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String reason;
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String number;
    @Schema(description = "申请类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer type;
    @Schema(description = "合同总价", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal contractValue;
    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    private String notes;
    @Schema(description = "申购内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<PurchaseDetailVo> purchaseDetailVoList;
}
