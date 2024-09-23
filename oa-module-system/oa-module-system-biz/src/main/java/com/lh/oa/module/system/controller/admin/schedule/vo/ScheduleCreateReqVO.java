package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 日程管理创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScheduleCreateReqVO extends ScheduleBaseVO {

//    @Schema(description = "开始时间")
//    @NotNull(message = "日程开始时间不能为空")
//    private LocalTime scheStartTime;
//
//    @Schema(description = "结束时间")
//    @NotNull(message = "日程结束时间不能为空")
//    private LocalTime scheEndTime;

}
