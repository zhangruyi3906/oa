package com.lh.oa.module.system.controller.admin.projectAttendanceRule.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 打卡规则（项目）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProjectAttendanceRuleListPageBaseVO extends PageParam {
    @Schema(description = "规则")
    private List<RuleVO> list;

    @Schema(description = "项目")
    private String projectName;

    @Schema(description = "项目id")
    private Long projectId;
}
