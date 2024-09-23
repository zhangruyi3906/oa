package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 会议记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeetingContentRespVO extends MeetingContentBaseVO {

    @Schema(description = "会议记录id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
