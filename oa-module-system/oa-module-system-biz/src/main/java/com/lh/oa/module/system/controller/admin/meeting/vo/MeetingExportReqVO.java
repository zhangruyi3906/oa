package com.lh.oa.module.system.controller.admin.meeting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会议组织 Excel 导出 Request VO，参数和 MeetingPageReqVO 是一致的")
@Data
public class MeetingExportReqVO {

    @Schema(description = "会议标题")
    private String title;

    @Schema(description = "会议描述")
    private String description;

    @Schema(description = "会议开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] startTime;

    @Schema(description = "会议结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] endTime;

    @Schema(description = "组织者")
    private String organizer;

    @Schema(description = "会议地点")
    private String location;

    @Schema(description = "会议状态")
    private Boolean status;

    @Schema(description = "记录创建时间")
    private Date createdAt;

    @Schema(description = "组织者id")
    private Long userId;

}
