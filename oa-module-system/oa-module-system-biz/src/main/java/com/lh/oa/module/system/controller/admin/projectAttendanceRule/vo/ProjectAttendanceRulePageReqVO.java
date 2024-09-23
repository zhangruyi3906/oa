package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.lh.oa.framework.common.pojo.PageParam;

@Schema(description = "管理后台 - 打卡规则（项目）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectAttendanceRulePageReqVO extends PageParam {

    @Schema(description = "用户id")
    private String punchName;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "打卡半径")
    private String punchRadius;

    @Schema(description = "打卡经纬度")
    private String latiLong;

    @ExcelProperty("项目")
    private String projectName;

}
