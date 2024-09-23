package com.lh.oa.module.bpm.controller.admin.fixAssetPurchase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author yangsheng
 */
@Schema(description = "管理后台 - 固定资产申购创建 RequestVo")
@Data
@ToString(callSuper = true)
public class FixAssetPurchaseCreateVo extends FixAssetPurchaseBaseVo{
}
