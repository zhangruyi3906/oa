package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 会议记录 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class MeetingContentBaseVO {

    @Schema(description = "会议ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议ID不能为空")
    private Integer meetingId;

    @Schema(description = "会议议题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议议题不能为空")
    private String topic;

    @Schema(description = "发言人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发言人不能为空")
    private String speaker;

    @Schema(description = "会议内容")
    private String content;

    @Schema(description = "记录创建时间")
    private Date createdAt;

    @Schema(description = "记录人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String writeName;

}
