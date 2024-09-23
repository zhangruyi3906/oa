package com.lh.oa.module.system.controller.admin.volumestatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 员工方量统计 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VolumeStatisticsRespVO extends VolumeStatisticsBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
