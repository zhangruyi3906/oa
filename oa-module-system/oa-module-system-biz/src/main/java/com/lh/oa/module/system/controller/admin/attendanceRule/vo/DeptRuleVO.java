package com.lh.oa.module.system.controller.admin.attendanceRule.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeptRuleVO {
    @Schema(description = "打卡半径（米）")
    @NotNull(message = "打卡范围不能为空")
    private Object punchRadius;

    @Schema(description = "打卡经度纬度对应的地址")
    @NotNull(message = "打卡经度纬度对应的地址不能为空")
    private String description;

    @Schema(description = "打卡经度纬度,格式为（经度，纬度）")
    @NotNull(message = "经纬度不能为空")
    private String latiLong;
}
