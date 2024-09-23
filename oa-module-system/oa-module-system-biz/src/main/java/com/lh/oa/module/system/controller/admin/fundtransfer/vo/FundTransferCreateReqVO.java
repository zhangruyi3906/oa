package com.lh.oa.module.system.controller.admin.fundtransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 资金划拨创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FundTransferCreateReqVO extends FundTransferBaseVO {

}
