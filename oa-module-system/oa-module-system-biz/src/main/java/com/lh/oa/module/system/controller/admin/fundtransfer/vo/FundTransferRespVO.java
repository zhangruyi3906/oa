package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Schema(description = "管理后台 - 资金划拨 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FundTransferRespVO extends FundTransferBaseVO {

    @Schema(description = "划拨ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "创建时间")
    private Date createTime;

}
