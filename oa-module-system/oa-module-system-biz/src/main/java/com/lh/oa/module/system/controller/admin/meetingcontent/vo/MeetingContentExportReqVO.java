package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "管理后台 - 会议记录 Excel 导出 Request VO，参数和 MeetingContentPageReqVO 是一致的")
@Data
public class MeetingContentExportReqVO {

    @Schema(description = "会议ID")
    private Integer meetingId;

    @Schema(description = "会议议题")
    private String topic;

    @Schema(description = "发言人")
    private String speaker;

    @Schema(description = "会议内容")
    private String content;

    @Schema(description = "记录创建时间")
    private Date createdAt;

    @Schema(description = "记录人")
    private String writeName;

}
