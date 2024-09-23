package com.lh.oa.module.system.controller.admin.jobcommission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 项目工种提成 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class JobCommissionBaseVO {

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目编号不能为空")
    private Long projectId;

    @Schema(description = "工种id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工种id不能为空")
    private Long jobId;

    @Schema(description = "基础提成", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "基础提成不能为空")
    private BigDecimal baseCommission;

    @Schema(description = "奖励提成", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "奖励提成不能为空")
    private BigDecimal bonusCommission;

}
