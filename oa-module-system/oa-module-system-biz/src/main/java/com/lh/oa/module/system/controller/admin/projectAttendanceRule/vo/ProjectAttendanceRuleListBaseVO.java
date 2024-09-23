package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAttendanceRuleListBaseVO {
    @Schema(description = "规则")
    @NotNull(message = "规则不能为空")
    private List<RuleVO> list;

    @Schema(description = "项目")
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    @Schema(description = "项目id")
    @NotNull(message = "项目不能为空")
    private Long projectId;

    private Integer punchType;

    private Integer allopatricStatus;

    private Integer syncHolidays;

    private String  punchTypeName;

    private String  workDays;

    private LocalTime endNextDay;

    private LocalTime flexibleCheckInStart;

    @Schema(description = "下班时间")
    private LocalTime flexibleCheckInEnd;
}
