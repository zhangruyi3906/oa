package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 考勤月统计 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class MonthStatisticsBaseVO {

    @Schema(description = "员工编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工编号不能为空")
    private Long userId;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long projectId;

    @Schema(description = "部门编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "编号不能为空")
    private Long deptId;

    @Schema(description = "考勤月份", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "考勤月份不能为空")
    private String attendanceMonth;

    @Schema(description = "本月总天数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalWorkingDays;

    @Schema(description = "实际工作天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际工作天数不能为空")
    private Integer actualWorkingDays;

    @Schema(description = "总加班小时数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总加班小时数不能为空")
    private Integer totalOvertimeHours;

    @Schema(description = "总旷工天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总旷工天数不能为空")
    private Integer totalLeaveDays;

    @Schema(description = "总异常天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总缺勤天数不能为空")
    private Integer totalAbsenceDays;

    private Integer tenantId;

    private String userName;

    private String deptName;

    private String projectName;

    @Schema(description = "类型，0 部门 1 项目")
    private Integer attStatus;

}
