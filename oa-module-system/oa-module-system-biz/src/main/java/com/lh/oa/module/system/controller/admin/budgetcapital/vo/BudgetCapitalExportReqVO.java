package com.lh.oa.module.system.controller.admin.budgetcapital.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 资金预算 Excel 导出 Request VO，参数和 BudgetCapitalPageReqVO 是一致的")
@Data
public class BudgetCapitalExportReqVO {

    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "预算周期")
    private String budgetPeriod;

    @Schema(description = "预算类型")
    private String budgetType;

    @Schema(description = "预算金额")
    private BigDecimal budgetAmount;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

}
