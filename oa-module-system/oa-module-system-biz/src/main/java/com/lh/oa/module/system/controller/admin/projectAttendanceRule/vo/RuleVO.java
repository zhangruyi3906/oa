package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleVO {
    @Schema(description = "打卡地点")
    @NotNull(message = "打卡地点不能为空")
    private String punchName;

    @Schema(description = "打卡半径")
    @NotNull(message = "打卡半径不能为空")
    private String punchRadius;

    @Schema(description = "打卡经纬度，经度纬度使用，隔开")
    @NotNull(message = "打卡经纬度不能为空")
    private String latiLong;

}
