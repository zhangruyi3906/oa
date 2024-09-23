package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 考勤月统计更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthStatisticsUpdateReqVO extends MonthStatisticsBaseVO {

    @Schema(description = "统计编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计编号不能为空")
    private Long id;

}
