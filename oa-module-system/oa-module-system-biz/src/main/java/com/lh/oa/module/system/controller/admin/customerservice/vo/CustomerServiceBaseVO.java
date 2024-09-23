package com.lh.oa.module.system.controller.admin.customerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 客户服务 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class CustomerServiceBaseVO {

    @Schema(description = "客户id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户id不能为空")
    private Long customerId;

    @Schema(description = "服务内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "服务内容不能为空")
    private String serviceContent;

    @Schema(description = "反馈")
    private String feedback;

    @Schema(description = "状态")
    private String status;

}
