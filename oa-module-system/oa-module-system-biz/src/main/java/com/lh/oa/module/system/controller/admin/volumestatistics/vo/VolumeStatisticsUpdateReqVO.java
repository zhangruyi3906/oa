package com.lh.oa.module.system.controller.admin.volumestatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 员工方量统计更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VolumeStatisticsUpdateReqVO extends VolumeStatisticsBaseVO {

    @Schema(description = "记录编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "记录编号不能为空")
    private Long id;

}
