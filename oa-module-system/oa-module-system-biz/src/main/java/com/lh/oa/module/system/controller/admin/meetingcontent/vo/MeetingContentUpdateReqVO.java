package com.lh.oa.module.system.controller.admin.meetingcontent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 会议记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MeetingContentUpdateReqVO extends MeetingContentBaseVO {

    @Schema(description = "会议记录id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "会议记录id不能为空")
    private Long id;

}
