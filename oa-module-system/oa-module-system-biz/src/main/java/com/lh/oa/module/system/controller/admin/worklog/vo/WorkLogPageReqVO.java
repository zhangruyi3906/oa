package com.lh.oa.module.system.controller.admin.worklog.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 员工工作日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkLogPageReqVO extends PageParam {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "日志日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] logDate;

    @Schema(description = "创建时间", example = "时间戳")
    private Long startTime;

    @Schema(description = "创建时间", example = "时间戳")
    private Long endTime;


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

    @Schema(description = "名字")
    private String userName;
}
