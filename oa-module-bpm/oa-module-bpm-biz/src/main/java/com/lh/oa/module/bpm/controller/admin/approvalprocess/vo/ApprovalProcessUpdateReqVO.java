package com.lh.oa.module.bpm.controller.admin.approvalprocess.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 项目立项更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ApprovalProcessUpdateReqVO extends ApprovalProcessBaseVO {

    @Schema(description = "项目立项ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "项目立项ID不能为空")
    private Long id;

}
