package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 考勤月统计 Excel 导出 Request VO，参数和 MonthStatisticsPageReqVO 是一致的")
@Data
public class MonthStatisticsExportReqVO {

    @Schema(description = "员工编号")
    private Long userId;

    @Schema(description = "项目编号")
    private Long projectId;

    @Schema(description = "部门编号")
    private Long deptId;

    @Schema(description = "考勤月份")
    private String attendanceMonth;

    @Schema(description = "总工作天数")
    private Integer totalWorkingDays;

    @Schema(description = "实际工作天数")
    private Integer actualWorkingDays;

    @Schema(description = "总加班小时数")
    private Integer totalOvertimeHours;

    @Schema(description = "总请假天数")
    private Integer totalLeaveDays;

    @Schema(description = "总缺勤天数")
    private Integer totalAbsenceDays;

}
