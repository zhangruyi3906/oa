package com.lh.oa.module.system.controller.admin.meeting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 会议组织 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeetingRespVO extends MeetingBaseVO {

    @Schema(description = "会议ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
