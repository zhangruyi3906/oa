package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 考勤月统计 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthStatisticsRespVO extends MonthStatisticsBaseVO {

    @Schema(description = "统计编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
