//package com.lh.oa.module.bpm.controller.admin.attendancecorrection.vo;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.*;
//import java.util.*;
//
//import com.alibaba.excel.annotation.ExcelProperty;
//
///**
// * 补卡流程 Excel VO
// *
// * @author 狗蛋
// */
//@Data
//public class CorrectionExcelVO {
//
//    @ExcelProperty("补卡申请ID")
//    private Long id;
//
//    @ExcelProperty("申请人ID")
//    private Long userId;
//
//    @ExcelProperty("申请人姓名")
//    private String userName;
//
//    @ExcelProperty("补卡原因")
//    private String reason;
//
//    @ExcelProperty("补卡时间")
//    private Date correctionTime;
//
//    @ExcelProperty("月份")
//    private String month;
//
//    @ExcelProperty("创建时间")
//    private Date createTime;
//
//    @ExcelProperty("补卡类型")
//    private String type;
//
//    @ExcelProperty("流程实例编号")
//    private String processInstanceId;
//
//    @ExcelProperty("审批状态")
//    private Boolean approvalStatus;
//
//}
