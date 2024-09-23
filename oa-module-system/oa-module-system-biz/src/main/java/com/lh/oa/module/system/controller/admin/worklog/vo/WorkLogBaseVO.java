package com.lh.oa.module.system.controller.admin.worklog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 员工工作日志 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WorkLogBaseVO {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "日志日期")
    private Date logDate;

    @Schema(description = "日志内容")
    private String logContent;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "字段描述")
    private String description;

    @Schema(description = "是否可修改")
    private Boolean isEditable;

    @Schema(description = "部门id")
    private Long deptId;
    @Schema(description = "提交时间")
    private String submitTime;

    private String userName;

}
