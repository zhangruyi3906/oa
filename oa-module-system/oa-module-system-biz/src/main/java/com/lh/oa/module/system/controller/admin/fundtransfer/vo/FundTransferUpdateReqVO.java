package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 资金划拨更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FundTransferUpdateReqVO extends FundTransferBaseVO {

    @Schema(description = "划拨ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "划拨ID不能为空")
    private Long id;

}
