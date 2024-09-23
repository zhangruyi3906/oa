package com.lh.oa.module.system.controller.admin.jobcommission.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 项目工种提成分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobCommissionPageReqVO extends PageParam {

    @Schema(description = "项目编号")
    private Long projectId;

    @Schema(description = "工种id")
    private Long jobId;

    @Schema(description = "基础提成")
    private BigDecimal baseCommission;

    @Schema(description = "奖励提成")
    private BigDecimal bonusCommission;

}
