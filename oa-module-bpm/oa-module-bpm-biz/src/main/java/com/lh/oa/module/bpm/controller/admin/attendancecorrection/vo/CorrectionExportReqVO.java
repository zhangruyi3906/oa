//package com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo;
//
//import lombok.*;
//import java.util.*;
//import io.swagger.v3.oas.annotations.media.Schema;
//import pojo.com.lh.oa.framework.common.PageParam;
//
//@Schema(description = "管理后台 - 补卡流程 Excel 导出 Request VO，参数和 CorrectionPageReqVO 是一致的")
//@Data
//public class CorrectionExportReqVO {
//
//    @Schema(description = "申请人ID")
//    private Long userId;
//
//    @Schema(description = "申请人姓名")
//    private String userName;
//
//    @Schema(description = "补卡原因")
//    private String reason;
//
//    @Schema(description = "补卡时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] correctionTime;
//
//    @Schema(description = "月份")
//    private String month;
//
//    @Schema(description = "创建时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private Date[] createTime;
//
//    @Schema(description = "补卡类型")
//    private String type;
//
//    @Schema(description = "流程实例编号")
//    private String processInstanceId;
//
//    @Schema(description = "审批状态")
//    private Boolean approvalStatus;
//
//}
