package com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 补卡流程更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CorrectionUpdateReqVO extends CorrectionBaseVO {

    @Schema(description = "补卡申请ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "补卡申请ID不能为空")
    private Long id;

}
