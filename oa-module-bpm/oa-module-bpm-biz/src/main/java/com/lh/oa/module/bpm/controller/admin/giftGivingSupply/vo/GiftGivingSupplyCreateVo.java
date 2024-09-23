package com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangfan
 * @since 2023/10/20 17:38
 */
@Schema(description = "管理后台 - 礼品赠送申请创建 RequestVo")
@Data
@ToString(callSuper = true)
public class GiftGivingSupplyCreateVo extends GiftGivingSupplyBaseVo {

}
