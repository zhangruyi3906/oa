package com.lh.oa.module.system.controller.admin.record.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 打卡记录更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecordUpdateReqVO extends RecordCreateReqVO {

    @Schema(description = "打卡记录ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "打卡记录ID不能为空")
    private Long id;



}
