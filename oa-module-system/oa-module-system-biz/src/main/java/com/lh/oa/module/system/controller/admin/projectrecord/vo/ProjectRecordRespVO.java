package com.lh.oa.module.system.controller.admin.projectrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 打卡记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectRecordRespVO extends ProjectRecordBaseVO {

    @Schema(description = "打卡记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
}
