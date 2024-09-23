package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 员工工种等级基础工资 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class JobLevelSalaryBaseVO {

    @Schema(description = "工种类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工种类型不能为空")
    private String jobCode;

    @Schema(description = "技术等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "技术等级不能为空")
    private Integer jobLevel;

    @Schema(description = "员工id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "员工id不能为空")
    private Long userId;

    @Schema(description = "基础工资", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "基础工资不能为空")
    private BigDecimal baseSalary;

}
