//package com.lh.oa.module.bpm.controller.admin.budgetapplication.vo;
//
//import lombok.*;
//import java.util.*;
//import io.swagger.v3.oas.annotations.media.Schema;
//import pojo.com.lh.oa.framework.common.PageParam;
//
//@Schema(description = "管理后台 - 资金预算申请 Excel 导出 Request VO，参数和 budgetApplicationPageReqVO 是一致的")
//@Data
//public class BudgetApplicationExportReqVO {
//
//    @Schema(description = "项目ID")
//    private Long projectId;
//
//    @Schema(description = "预算周期")
//    private String budgetPeriod;
//
//    @Schema(description = "预算类型")
//    private String budgetType;
//
//    @Schema(description = "预算金额")
//    private BigDecimal budgetAmount;
//
//    @Schema(description = "创建时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] createTime;
//
//    @Schema(description = "审批状态")
//    private Boolean approvalStatus;
//
//    @Schema(description = "流程实例的编号")
//    private String processInstanceId;
//
//}
