package com.lh.oa.module.system.controller.admin.salarysettlement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
public class MonthAttendance {
    @Schema(description = "员工编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> userIds;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long projectId;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> deptIds;

    @Schema(description = "考勤月份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "考勤月份不能为空")
    private Integer attendanceMonth;

    @Schema(description = "统计类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer reType;
}
