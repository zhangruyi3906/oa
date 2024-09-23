package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 资金划拨 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class FundTransferBaseVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @Schema(description = "划拨金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "划拨金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "项目名称不能为空")
    private String projectName;

}
