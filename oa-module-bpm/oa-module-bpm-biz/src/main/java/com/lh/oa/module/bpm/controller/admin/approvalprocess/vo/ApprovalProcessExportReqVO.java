//package com.lh.oa.module.bpm.controller.admin.approvalprocess.vo;
//
//import lombok.*;
//import java.util.*;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//@Schema(description = "管理后台 - 项目立项 Excel 导出 Request VO，参数和 ApprovalProcessPageReqVO 是一致的")
//@Data
//public class ApprovalProcessExportReqVO {
//
//    @Schema(description = "项目名称")
//    private String projectName;
//
//    @Schema(description = "项目开始日期")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] startDate;
//
//    @Schema(description = "项目结束日期")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] endDate;
//
//    @Schema(description = "项目经理ID")
//    private Long projectManagerId;
//
//    @Schema(description = "审批状态")
//    private Boolean approvalStatus;
//
//    @Schema(description = "流程实例的编号")
//    private String processInstanceId;
//
//    @Schema(description = "创建时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] createTime;
//
//}
