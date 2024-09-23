package com.lh.oa.module.system.controller.admin.salary.salary;

import com.lh.oa.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SalaryPageReqVO extends PageParam {
    @Schema(description = "月份")
    private String month;
    @Schema(description = "手机号")
    private String mobile;
    private String username;
}
