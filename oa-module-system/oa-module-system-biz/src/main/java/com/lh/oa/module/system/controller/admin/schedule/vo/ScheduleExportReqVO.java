package com.lh.oa.module.system.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程管理 Excel 导出 Request VO，参数和 SchedulePageReqVO 是一致的")
@Data
public class ScheduleExportReqVO {

    @Schema(description = "日程标题")
    private String title;

    @Schema(description = "日程描述")
    private String description;

    @Schema(description = "日程时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Time[] expireTime;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "是否过期")
    private Boolean expired;

    @Schema(description = "创建者id")
    private Long userId;

    @Schema(description = "日程日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] expireDate;

    @Schema(description = "开始时间")
    private Date scheStartTime;

    @Schema(description = "结束时间")
    private Date scheEndTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "日程状态")
    private Integer status;

}
