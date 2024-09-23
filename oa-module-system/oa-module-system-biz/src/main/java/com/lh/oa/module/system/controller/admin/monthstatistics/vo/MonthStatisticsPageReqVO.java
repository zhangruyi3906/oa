package com.lh.oa.module.system.controller.admin.monthstatistics.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 考勤月统计分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthStatisticsPageReqVO extends PageParam {

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

    private Integer tenantId;

    @Schema(description = "类型，0 部门 1 项目")
    private Integer attStatus;

}
