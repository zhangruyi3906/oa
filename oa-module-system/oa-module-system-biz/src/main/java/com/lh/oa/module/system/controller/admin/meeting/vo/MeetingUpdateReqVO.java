package com.lh.oa.module.system.controller.admin.meeting.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 会议组织更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeetingUpdateReqVO extends MeetingBaseVO {

    @Schema(description = "会议ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议ID不能为空")
    private Long id;

}
