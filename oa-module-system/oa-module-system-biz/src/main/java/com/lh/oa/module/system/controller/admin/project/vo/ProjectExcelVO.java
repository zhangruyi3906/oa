package com.lh.oa.module.system.controller.admin.project.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 项目 Excel VO
 *
 * @author 狗蛋
 */
@Data
public class ProjectExcelVO {

    @ExcelProperty("主键ID")
    private Integer id;

    @ExcelProperty("所属组织ID")
    private Integer orgId;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("简称")
    private String simpleName;

    @ExcelProperty("简介")
    private String simpleDescription;

    @ExcelProperty("类型")
    private String type;

    @ExcelProperty("类型")
    private String typeVal;


    @ExcelProperty("效果图ID")
    private Integer effectGraphFileId;

    @ExcelProperty("效果图URL")
    private String effectGraphFileUrl;

    @ExcelProperty("平面图ID")
    private Integer planarGraphFileId;

    @ExcelProperty("平面图URL")
    private String planarGraphFileUrl;

    @ExcelProperty("计划开始时间")
    private Integer planStartTime;

    @ExcelProperty("计划结束时间")
    private Integer planEndTime;

    @ExcelProperty("负责人")
    private String directorName;

    @ExcelProperty("负责人电话")
    private String directorMobile;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("经度")
    private BigDecimal longitude;

    @ExcelProperty("纬度")
    private BigDecimal latitude;

    @ExcelProperty("施工单位")
    private String constructUnit;

    @ExcelProperty("建设单位")
    private String workUnit;

    @ExcelProperty("监理单位")
    private String superviseUnit;

    @ExcelProperty("设计单位")
    private String designUnit;

    @ExcelProperty("勘察单位")
    private String surveyUnit;

    @ExcelProperty("微官网ID")
    private Integer microWebsiteId;

    @ExcelProperty("排序")
    private Integer orderNumber;

    @ExcelProperty("创建时间")
    private Integer createdTime;

    @ExcelProperty("创建人")
    private Integer createdBy;

    @ExcelProperty("修改时间")
    private Integer modifiedTime;

    @ExcelProperty("修改人")
    private Integer modifiedBy;

    @ExcelProperty("统一标志")
    private Integer flag;

}
