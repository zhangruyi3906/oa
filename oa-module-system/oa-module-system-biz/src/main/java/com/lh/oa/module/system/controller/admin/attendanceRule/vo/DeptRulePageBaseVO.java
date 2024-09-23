package com.lh.oa.module.system.controller.admin.attendanceRule.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 打卡规则（部门）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeptRulePageBaseVO extends PageParam {
    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    private String punchTypeName;
    private String workDays;
    private Integer syncHolidays;

    @Schema(description = "规则")
    private List<DeptRuleVO> list;
}
