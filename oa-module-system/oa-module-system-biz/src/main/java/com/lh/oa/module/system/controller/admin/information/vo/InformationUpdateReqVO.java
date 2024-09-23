package com.lh.oa.module.system.controller.admin.information.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 员工信息更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InformationUpdateReqVO extends InformationBaseVO {

    @Schema(description = "员工唯一标识符", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工唯一标识符不能为空")
    private Long id;

}
