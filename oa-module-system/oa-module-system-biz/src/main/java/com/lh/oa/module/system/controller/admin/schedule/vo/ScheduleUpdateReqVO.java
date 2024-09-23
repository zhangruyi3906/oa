package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 日程管理更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScheduleUpdateReqVO extends ScheduleBaseVO {

    @Schema(description = "日程记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "日程记录ID不能为空")
    private Long id;

//    @Schema(description = "开始时间")
//    @NotNull(message = "日程开始时间不能为空")
//    private LocalTime scheStartTime;
//
//    @Schema(description = "结束时间")
//    @NotNull(message = "日程结束时间不能为空")
//    private LocalTime scheEndTime;

}
