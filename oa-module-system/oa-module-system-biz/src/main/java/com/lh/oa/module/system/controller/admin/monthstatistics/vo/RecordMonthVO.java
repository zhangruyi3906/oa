package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecordMonthVO {
    @Schema(description = "员工编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> userIds;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> projectId;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> deptIds;

    @Schema(description = "考勤月份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "考勤月份不能为空")
    private String attendanceMonth;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "休息天数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer restDays;

    @Schema(description = "统计类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer reType;
}
