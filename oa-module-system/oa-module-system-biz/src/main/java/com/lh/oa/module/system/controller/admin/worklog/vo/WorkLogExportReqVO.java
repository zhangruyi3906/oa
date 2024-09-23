package com.lh.oa.module.system.controller.admin.worklog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 员工工作日志 Excel 导出 Request VO，参数和 WorkLogPageReqVO 是一致的")
@Data
public class WorkLogExportReqVO {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "日志日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] logDate;

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

}
