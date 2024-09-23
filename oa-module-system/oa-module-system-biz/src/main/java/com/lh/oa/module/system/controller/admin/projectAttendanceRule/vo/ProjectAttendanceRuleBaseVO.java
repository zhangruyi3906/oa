package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * 打卡规则（项目） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class ProjectAttendanceRuleBaseVO {

    @Schema(description = "打卡地点")
    @NotNull(message = "打卡地点不能为空")
    private String punchName;

    @Schema(description = "项目id")
    @NotNull(message = "项目不能为空")
    private Long projectId;

    @Schema(description = "打卡半径")
    @NotNull(message = "打卡半径不能为空")
    private String punchRadius;

    @Schema(description = "打卡经纬度，经度纬度使用，隔开")
    @NotNull(message = "打卡经纬度不能为空")
    private String latiLong;

    @Schema(description = "项目")
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    private Integer punchType;

    private Integer allopatricStatus;

    private Integer syncHolidays;

    private String punchTypeName;

    private String workDays;

    private LocalTime endNextDay;

    @Schema(description = "上班时间")
    private LocalTime flexibleCheckInStart;

    @Schema(description = "下班时间")
    private LocalTime flexibleCheckInEnd;
}
