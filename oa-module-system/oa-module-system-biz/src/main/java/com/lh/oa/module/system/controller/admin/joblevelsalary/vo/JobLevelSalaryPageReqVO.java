package com.lh.oa.module.system.controller.admin.joblevelsalary.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 员工工种等级基础工资分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobLevelSalaryPageReqVO extends PageParam {

    @Schema(description = "工种类型")
    private String jobCode;

    @Schema(description = "技术等级")
    private Integer jobLevel;

    @Schema(description = "员工id")
    private Long userId;

    @Schema(description = "基础工资")
    private BigDecimal baseSalary;

}
