package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 日程管理 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScheduleRespVO extends ScheduleBaseVO {

    @Schema(description = "日程记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    private Long startTime;

    private Long endTime;

}
