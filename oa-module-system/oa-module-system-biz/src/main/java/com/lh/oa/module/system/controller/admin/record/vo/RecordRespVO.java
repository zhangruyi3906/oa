package com.lh.oa.module.system.controller.admin.record.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 打卡记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecordRespVO extends RecordBaseVO {

    @Schema(description = "打卡记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
