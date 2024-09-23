package com.lh.oa.module.system.controller.admin.projectrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectRecordBaseVO {
    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "签到时间")
    private LocalDateTime checkInTime;

    @Schema(description = "签退时间")
    private LocalDateTime checkOutTime;

    @Schema(description = "签到状态")
    private Byte checkInStatus;

    @Schema(description = "签退状态")
    private Byte checkOutStatus;

    @Schema(description = "打卡年月日")
    private LocalDate punchDate;

    @Schema(description = "签到类型（0 部门，1 项目）")
    private Byte attStatus;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "地点备注")
    private String remarkIn;

    private String userName;

    private String projectName;
}
