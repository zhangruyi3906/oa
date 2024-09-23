package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Schema(description = "管理后台 - 会议记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeetingContentPageReqVO extends PageParam {

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
