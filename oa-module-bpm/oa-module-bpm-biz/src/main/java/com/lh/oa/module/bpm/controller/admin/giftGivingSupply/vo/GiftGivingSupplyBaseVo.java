package com.lh.oa.module.bpm.controller.admin.giftGivingSupply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zhangfan
 * @since 2023/10/20 17:38
 */
@Schema(description = "管理后台 - 礼品赠送申请 BaseVo")
@Data
@ToString(callSuper = true)
public class GiftGivingSupplyBaseVo {

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
    @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deptId;
    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;
    @Schema(description = "办公地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String place;
    @Schema(description = "办公地点名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String placeName;
    @Schema(description = "送礼内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<GiftDetailVo> giftDetailVoList;

}
