package com.lh.oa.module.bpm.controller.admin.costReimburse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 费用报销申请 BaseVo")
@Data
@ToString(callSuper = true)
public class CostReimburseBaseVo {
    @Schema(description = "费用所属公司", requiredMode = Schema.RequiredMode.REQUIRED)
    private String companyName;
    @Schema(description = "付款项目", requiredMode = Schema.RequiredMode.REQUIRED)
    private String projectName;
    @Schema(description = "报销人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(description = "报销人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "报销部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;
    @Schema(description = "报销部门", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
    @Schema(description = "报销日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date reimburseDate;
    @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "现金")
    private String payWay;
    @Schema(description = "收款人", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private String payee;
    @Schema(description = "收款账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[0-9]{9,18}$", message = "当前账号输入有误或格式不正确，请检查后重新填写")
    private String account;
    @Schema(description = "收款银行", requiredMode = Schema.RequiredMode.REQUIRED)
    private String bank;
    @Schema(description = "费用详细说明", requiredMode = Schema.RequiredMode.REQUIRED)
    private String illustrate;
    @Schema(description = "附件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String annex;
    @Schema(description = "报销总额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal totalPrice;
    @Schema(description = "费用明细", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<CostDetailVo> costDetailVoList;
}
