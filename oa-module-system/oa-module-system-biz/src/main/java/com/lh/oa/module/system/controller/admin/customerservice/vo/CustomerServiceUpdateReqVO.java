package com.lh.oa.module.system.controller.admin.customerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 客户服务更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomerServiceUpdateReqVO extends CustomerServiceBaseVO {

    @Schema(description = "服务id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "服务id不能为空")
    private Long id;

}
