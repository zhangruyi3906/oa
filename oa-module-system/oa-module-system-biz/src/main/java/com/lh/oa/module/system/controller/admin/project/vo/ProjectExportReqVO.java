package com.lh.oa.module.system.controller.admin.project.vo;

import lombok.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目 Excel 导出 Request VO，参数和 ProjectPageReqVO 是一致的")
@Data
public class ProjectExportReqVO {

    @Schema(description = "所属组织ID")
    private Integer orgId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "简称")
    private String simpleName;

    @Schema(description = "简介")
    private String simpleDescription;

    @Schema(description = "类型")
    private String type;

    private String typeVal;

    @Schema(description = "效果图ID")
    private Integer effectGraphFileId;

    @Schema(description = "效果图URL")
    private String effectGraphFileUrl;

    @Schema(description = "平面图ID")
    private Integer planarGraphFileId;

    @Schema(description = "平面图URL")
    private String planarGraphFileUrl;

    @Schema(description = "计划开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] planStartTime;

    @Schema(description = "计划结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] planEndTime;

    @Schema(description = "负责人")
    private String directorName;

    @Schema(description = "负责人电话")
    private String directorMobile;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal longitude;

    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Schema(description = "施工单位")
    private String constructUnit;

    @Schema(description = "建设单位")
    private String workUnit;

    @Schema(description = "监理单位")
    private String superviseUnit;

    @Schema(description = "设计单位")
    private String designUnit;

    @Schema(description = "勘察单位")
    private String surveyUnit;

    @Schema(description = "微官网ID")
    private Integer microWebsiteId;

    @Schema(description = "排序")
    private Integer orderNumber;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] createdTime;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "修改时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] modifiedTime;

    @Schema(description = "修改人")
    private Integer modifiedBy;

    @Schema(description = "统一标志")
    private Integer flag;

}
