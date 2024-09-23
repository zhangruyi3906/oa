package com.lh.oa.module.system.controller.admin.meeting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

/**
 * 会议组织 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class MeetingBaseVO {

    @Schema(description = "会议标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议标题不能为空")
    private String title;

    @Schema(description = "会议描述")
    private String description;

    @Schema(description = "会议开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议开始时间不能为空")
    private Date startTime;

    @Schema(description = "会议结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议结束时间不能为空")
    private Date endTime;

    @Schema(description = "组织者", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组织者不能为空")
    private String organizer;

    @Schema(description = "会议地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议地点不能为空")
    private String location;

    @Schema(description = "会议状态")
    private Boolean status;

    @Schema(description = "记录创建时间")
    private Date createdAt;

    @Schema(description = "组织者id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组织者id不能为空")
    private Long userId;

}
