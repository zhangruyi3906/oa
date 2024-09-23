package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * 打卡规则（部门） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class AttendanceRuleBaseVO {

    @Schema(description = "部门ID")
    @NotNull(message="部门id不能为空")
    private Long deptId;

    @Schema(description = "部门名称")
    @NotNull(message = "部门名称不能为空")
    private String deptName;

    @Schema(description = "打卡半径（米）")
    @NotNull(message = "打卡范围不能为空")
    private Object punchRadius;

    @Schema(description = "上班时间")
    @NotNull(message = "上班时间不能为空")
    private LocalTime flexibleCheckInStart;

    @Schema(description = "下班时间")
    @NotNull(message = "下班时间不能为空")
    private LocalTime flexibleCheckInEnd;

    @Schema(description = "打卡经度纬度对应的地址")
    private String description;

    @Schema(description = "打卡经度纬度")
    @NotNull(message = "经纬度不能为空")
    private String latiLong;


    private String punchTypeName;
    private String workDays;
    private Integer syncHolidays;


}
