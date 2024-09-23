package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

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
public class DeptRuleListVO {
    @Schema(description = "部门ID")
    @NotNull(message = "部门id不能为空")
    private Long deptId;

    @Schema(description = "部门名称")
    @NotNull(message = "部门名称不能为空")
    private String deptName;

    @Schema(description = "规则")
    @NotNull(message = "规则不能为空")
    private List<DeptRuleVO> list;

    private String punchTypeName;
    private String workDays;
    private Integer syncHolidays;
    @Schema(description = "上班时间")
    @NotNull(message = "上班时间不能为空")
    private LocalTime flexibleCheckInStart;

    @Schema(description = "下班时间")
    @NotNull(message = "下班时间不能为空")
    private LocalTime flexibleCheckInEnd;
}
