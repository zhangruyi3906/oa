package com.lh.oa.module.system.controller.admin.information.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 员工信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InformationRespVO extends InformationBaseVO {

    @Schema(description = "员工唯一标识符", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

}
